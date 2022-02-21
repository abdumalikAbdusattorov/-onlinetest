import React, {useState, useEffect} from "react";
import {
    Badge,
    Col,
    NavLink,
    Collapse,
    Navbar,
    NavbarToggler,
    Nav,
    NavItem,
    Row,
    Table,
    UncontrolledTooltip, Button, Card, CardBody
} from "reactstrap";
import axios from "axios";
import Pagination from "react-js-pagination";
import { IoIosCheckmark  ,IoIosDoneAll} from "react-icons/io";

export default function Comment() {
    useEffect(() => {
        if (!stopEffect) {
            if (!stopDidmountFunction) {
                axios.get('http://localhost/api/user/me', {headers: {"Authorization": localStorage.getItem('OnlineToken')}})
                    .then(res => {
                        //console.log(res,"me ==========");
                        if (res.data.object != null) {
                            res.data.object.roles.map(role => {
                                if (role.name === 'ROLE_ADMIN') {
                                    setIsAdmin(true);
                                    axios.get('http://localhost/api/comment/getViewedCount?viewed=' + false, {headers: {"Authorization": localStorage.getItem('OnlineToken')}})
                                        .then(res => {
                                            //console.log(res.data.object,"getViewedCount=============================================");
                                            setViewedComment(res.data.object);
                                        });
                                    axios.get('http://localhost/api/comment/getCommentByPagebleByViewed?page=0&size=10&viewed=' + viewed, {headers: {"Authorization": localStorage.getItem('OnlineToken')}})
                                        .then(res => {
                                            //console.log(res.data.object,"dsdsdsdsds");
                                            setCommentArray(res.data.object);
                                            setTotalElement(res.data.totalElements);
                                            setStopEffect(true);
                                        })
                                }
                                if (role.name === 'ROLE_USER') {
                                    setIsUser(true);
                                    axios.get('http://localhost/api/comment/getCommentByUser?page=0&size=10', {headers: {"Authorization": localStorage.getItem('OnlineToken')}})
                                        .then(res => {
                                            console.log(res,"userni kommenti");
                                            setCommentArray(res.data.object);
                                            setTotalElement(res.data.totalElements);
                                            setStopEffect(true)
                                        })
                                }
                            })
                        }
                        setStopDidmountFunction(true)
                    })
            }
        }
    }, []);

    const [stopDidmountFunction, setStopDidmountFunction] = useState(false);
    const [isAdmin, setIsAdmin] = useState(false);
    const [isUser, setIsUser] = useState(false);
    const [viewedComment,setViewedComment] = useState(false);
    const [isOpen, setIsOpen] = useState(false);
    const [commentArray, setCommentArray] = useState([]);
    const [totalElement, setTotalElement] = useState(0);
    const [stopEffect, setStopEffect] = useState(false);
    const [activePage, setActivePage] = useState(1);
    const [viewed,setviewed] = useState(false);
    const [isOpenCollapse,setIsOpenCollapse] = useState(-1);
    const [bool,setBool] = useState(false);

    const newOrOldComment =(e) =>{
        //console.log(e.target.value)
        let val = e.target.value;
        axios.get('http://localhost/api/comment/getCommentByPagebleByViewed?page=0&size=10&viewed=' + val, {headers: {"Authorization": localStorage.getItem('OnlineToken')}})
            .then(res => {
                //console.log(res.data.object);
                setCommentArray(res.data.object);
                setTotalElement(res.data.totalElements);
                setIsOpenCollapse(-1);
            });


    };



    const collapseIsOpen = (index) => {
        if(bool){
            setBool(!bool);
            setIsOpenCollapse(-1)
        }else {
            setBool(!bool);
            setIsOpenCollapse(index)
        }

        /*console.log(index);
        console.log(isOpenCollapse);*/
    };
    const toggle = () => setIsOpen(!isOpen);

    const handlePageChange = (pageNumber) => {
        if (isAdmin){
            setActivePage(pageNumber);
            let page = pageNumber-1;
            axios.get('http://localhost/api/comment/getCommentByPagebleByViewed?page=' + page + '&size=10&viewed=' + viewed, {headers: {"Authorization": localStorage.getItem('OnlineToken')}})
                .then(res => {
                    //console.log(res.data.object);
                    setCommentArray(res.data.object);
                    setTotalElement(res.data.totalElements);
                });
        }
        if (isUser){
            setActivePage(pageNumber);
            let page = pageNumber - 1;
            axios.get('http://localhost/api/comment/getCommentByUser?page='  + page + '&size=10',{headers: {"Authorization": localStorage.getItem('OnlineToken')}})
                .then(res => {
                    console.log(res);
                    setCommentArray(res.data.object);
                    setTotalElement(res.data.totalElements);
                });
        }

    };

    const commentIsViewed = (id) => {
            //console.log(id);
            axios.get('http://localhost/api/comment/' + id, {headers: {"Authorization": localStorage.getItem('OnlineToken')}})
                .then(res => {
                    axios.get('http://localhost/api/comment/getCommentByPagebleByViewed?page=0&size=10&viewed=' + viewed, {headers: {"Authorization": localStorage.getItem('OnlineToken')}})
                        .then(res => {
                            //console.log(res.data.object);
                            setCommentArray(res.data.object);
                            setTotalElement(res.data.totalElements);
                            setStopEffect(true);
                            axios.get('http://localhost/api/comment/getViewedCount?viewed=' + false, {headers: {"Authorization": localStorage.getItem('OnlineToken')}})
                                .then(res => {
                                    //console.log(res.data.object,"getViewedCount=============================================");
                                    setViewedComment(res.data.object);
                                });
                        })
                });


    };

    return (
        <div>
            <Row className="m-0 p-0">
                <Col>
                    {isAdmin ?
                        <Navbar color="light" light expand="md">
                            <NavbarToggler onClick={toggle}/>
                            <Collapse navbar>
                                <Nav className="mr-auto p-0" navbar>
                                    <NavItem>
                                        <NavLink href="/adminCabinet">Test</NavLink>
                                    </NavItem>
                                    <NavItem>
                                        <NavLink href="/block">Block</NavLink>
                                    </NavItem>
                                    <NavItem>
                                        <NavLink href="/testBlock">TestBlock</NavLink>
                                    </NavItem>
                                    <NavItem>
                                        <NavLink href="/historyUser">HistoryUser</NavLink>
                                    </NavItem>
                                    <NavItem>
                                        <NavLink href="/comment">Comment <Badge color="primary"
                                                                                pill>{viewedComment ? viewedComment : ''}</Badge></NavLink>
                                    </NavItem>
                                    <NavItem>
                                        <NavLink href="/subject">Subject</NavLink>
                                    </NavItem>
                                    <NavItem>
                                        <NavLink href="/">Chiqish</NavLink>
                                    </NavItem>
                                </Nav>
                            </Collapse>
                        </Navbar>
                        : isUser ?
                            <Navbar color="light" light expand="md">
                                <NavbarToggler onClick={toggle}/>
                                <Collapse navbar>
                                    <Nav className="mr-auto" navbar>
                                        <NavItem>
                                            <NavLink href="/solveTest">Test ishlash</NavLink>
                                        </NavItem>
                                        <NavItem>
                                            <NavLink href="/ownHistory">Tarix</NavLink>
                                        </NavItem>
                                        <NavItem>
                                            <NavLink href="/comment">Commentlar</NavLink>
                                        </NavItem>
                                        <NavItem>
                                            <NavLink href="/">Chiqish</NavLink>
                                        </NavItem>
                                    </Nav>
                                </Collapse>
                            </Navbar>
                            : ' '}
                </Col>
            </Row>
            <Row>
                <Col className="my-2">
                    <h1>Comment CRUD</h1>
                    {isAdmin ?
                        <select className="w-25" name="select" id="exampleSelect" onChange={newOrOldComment}>
                            <option value="false">Yangi xabarlar</option>
                            <option value="true">Eski xabarlar</option>
                        </select>
                        : isUser ? ' ' : ' '}
                </Col>
            </Row>
            <Row>
                <Table className="w-75">
                    <thead>
                    <tr>
                        <th>№</th>
                        {isAdmin ? <th>Foydalanuvchi</th>
                            : ' '
                        }
                        <th>Sovol</th>
                        <th>Izoh</th>

                            <th>Ko'rildi</th>
                    </tr>
                    </thead>
                    <tbody>
                    {commentArray ? commentArray.map((item, index) =>

                        <tr key={index}>
                            <td>{(activePage * 10) + index + 1 - 10}</td>

                            {isAdmin ?
                                <td>
                                <span style={{color: "blue"}}
                                      id="UncontrolledTooltipExample">{item.resUser.phoneNumber}</span>
                                    <UncontrolledTooltip placement="top" target="UncontrolledTooltipExample">
                                        {item.resUser.firstName} {item.resUser.lastName}
                                    </UncontrolledTooltip>
                                </td>
                                : ' '
                            }


                            {isAdmin ?
                                <td width="250px">
                                    <Button color="primary" onClick={() => collapseIsOpen(index)}
                                            style={{marginBottom: '1rem'}}>Savolni
                                        ko'rish</Button>
                                    <Collapse isOpen={isOpenCollapse === index}>
                                        <Card>
                                            <CardBody>
                                                <span>{item.resQuestion.question}</span>
                                                {item.resQuestion.resAnswers.map((item1) => <p>{item1.answer}</p>)}
                                            </CardBody>
                                        </Card>
                                    </Collapse>
                                </td>
                                : isUser ? <td width="250px">
                                    <Button color="primary" onClick={() => collapseIsOpen(index)}
                                            style={{marginBottom: '1rem'}}>Savolni
                                        ko'rish</Button>
                                    <Collapse isOpen={isOpenCollapse === index}>
                                        <Card>
                                            <CardBody>
                                                <span>{item.resQuestion.question}</span>
                                                {item.resQuestion.resAnswers.map((item1) => <p>{item1.answer}</p>)}
                                            </CardBody>
                                        </Card>
                                    </Collapse>
                                </td> : ' '}


                            <td>{item.commentText}</td>

                            {isAdmin ?
                                <td>
                                    {item.viewed ? ' ' :
                                        <Button color="primary"
                                                onClick={() => commentIsViewed(item.id)}>Ko'rildi</Button>}
                                </td>
                                : isUser ?
                                    <td>{item.viewed ? <h3><IoIosDoneAll/></h3> : <h3><IoIosCheckmark/></h3>}</td>
                                    : ' '}


                        </tr>
                    ) : ''}
                    </tbody>
                </Table>
            </Row>
            <Row>
                <Col>
                    <Pagination
                        activePage={activePage}
                        itemsCountPerPage={10}
                        totalItemsCount={totalElement}
                        pageRangeDisplayed={5}
                        onChange={handlePageChange.bind(this)} itemClass="page-item"
                        linkClass="page-link"
                    />
                </Col>
            </Row>

        </div>

    );
}