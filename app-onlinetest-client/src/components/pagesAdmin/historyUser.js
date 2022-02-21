import React, {useEffect, useState} from "react";
import {
    Col,
    Collapse,
    Badge,
    Nav,
    Navbar,
    NavbarToggler,
    NavItem,
    NavLink,
    Row,
    Table, NavbarBrand, Button, ModalHeader, ModalBody, ModalFooter, Modal, UncontrolledTooltip
} from "reactstrap";
import axios from "axios";
import Pagination from "react-js-pagination";

export default function HistoryUser() {
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
                                }
                                if (role.name === 'ROLE_USER') {
                                    setIsUser(true)
                                }
                            })
                        }
                        setStopDidmountFunction(true)
                    })
            }
            axios.get('http://localhost/api/historyUser/getHistoryByPageble?page=0&size=10', {headers: {"Authorization": localStorage.getItem('OnlineToken')}})
                .then(res => {
                    console.log(res.data.object, "mana user istoriyasi");
                    setHistoryUserArray(res.data.object);
                    setTotalElement(res.data.totalElements);
                    setStopEffect(true)
                })
        }

    }, []);
    const [stopDidmountFunction, setStopDidmountFunction] = useState(false)
    const [isAdmin, setIsAdmin] = useState(false);
    const [isUser, setIsUser] = useState(false);
    const [viewedComment,setViewedComment] = useState(false);
    const [isOpen, setIsOpen] = useState(false);
    const [addHistoryUserModal, setAddHistoryUserModal] = useState(false);
    const [historyUserArray, setHistoryUserArray] = useState([]);
    const [totalElement, setTotalElement] = useState(0);
    const [modal, setModal] = useState(false);
    const [showDeleteModal, setShowDeleteModal] = useState(false);
    const [stopEffect, setStopEffect] = useState(false);
    const [currentHistoryUser, setCurrentHistoryUser] = useState('');
    const [tempHistoryUserId, setTempHistoryUserId] = useState('');
    const [activePage, setActivePage] = useState(1);
    const [answersListModal, setAnswersListModal] = useState(false);
    const [tempAnswersList, setTempAnswersList] = useState([]);
    const [tempQuestionList, setTempQuestionList] = useState([]);


    const editBlock = (item) => {
        setAddHistoryUserModal(!addHistoryUserModal);
        setCurrentHistoryUser(item);
        //console.log(item);
    };
    const deleteHistoryUser = (id) => {
        setTempHistoryUserId(id);
        setShowDeleteModal(!showDeleteModal);
    };
    const addHistoryUser = () => {
        setAddHistoryUserModal(!addHistoryUserModal);
        setCurrentHistoryUser('');
    };
    const handlePageChange = (pageNumber) => {
        setActivePage(pageNumber);
        let page = pageNumber - 1;
        axios.get('http://localhost/api/historyUser/getHistoryByPageble?page=' + page + '&size=10', {headers: {"Authorization": localStorage.getItem('OnlineToken')}})
            .then(res => {
                //console.log(res);
                setHistoryUserArray(res.data.object);
                setTotalElement(res.data.totalElements);
            })
    };

    const showTestAnswers = (answersList,question) => {
        //console.log(answersList , "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        //console.log(question, "questionList");
        setTempAnswersList(answersList);
        setTempQuestionList(question);
        setAnswersListModal(!answersListModal);

    };
    const closeAnswersListModal = () => setAnswersListModal(!answersListModal);


    const toggle = () => setIsOpen(!isOpen);
    return (
        <div>
            <Row>
                <Col>
                    {isAdmin ?
                        <Navbar color="light" light expand="md">
                            <NavbarToggler onClick={toggle}/>
                            <Collapse navbar>
                                <Nav className="mr-auto" navbar>
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
                                            <NavLink href="/comment">Comment</NavLink>
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
                <Col>
                    <h1>HistoryUser CRUD</h1>
                </Col>
            </Row>
            <Row>
                <Table className="text-center">
                    <thead>
                    <tr>
                        <th>№</th>
                        {isAdmin ?
                            <th>User</th>
                            : ''}
                        <th>Ishlangan vaqti</th>
                        <th>Test Block</th>
                        {isAdmin ?
                            <th>From Bot</th>
                            : ''}
                        <th>Answers</th>
                        <th>Total Score</th>
                        <th>Max Score</th>
                    </tr>
                    </thead>
                    <tbody>
                    {historyUserArray ? historyUserArray.map((item, index) =>
                        //console.log(item.resTestBlock.resTestWithScoreList, "item manaaaaaaa")

                        <tr scope="row">
                            <td>{(activePage * 10) + index + 1 - 10}</td>
                            {isAdmin ?
                                <td>
                                     <span style={{color: "blue"}}
                                           id="UncontrolledTooltipExample">{item.resUser.phoneNumber}</span>
                                    <UncontrolledTooltip placement="top" target="UncontrolledTooltipExample">
                                        {item.resUser.firstName} {item.resUser.lastName}
                                    </UncontrolledTooltip>
                                </td>
                                : ''}
                            <td>{item.createdAt.substring(0,10)} | {item.createdAt.substring(11,16)}</td>
                            <td>{item.resTestBlock.resBlock.nameUz}</td>

                            {isAdmin ?
                                <td>
                                    {item.fromBot ?
                                        <p>&#x2611;</p> : <p>&#x2715;</p>
                                    }
                                </td>
                                : ''}
                            <td key={index}>
                                {/* {item.resTestBlock.resTestWithScoreList ? item.resTestBlock.resTestWithScoreList.map((item1) =>
                                    console.log(item1.resTest.resQuestionList,"resTestWithScoreList")
                                    item1.resTest.resQuestionList ? item1.resTest.resQuestionList.map((item2) =>
                                        console.log(item2.question)
                                    ) : ' '*/}

                                <Button color="primary"
                                        onClick={() => showTestAnswers(item.resAnswerList, item.resTestBlock.resTestWithScoreList)}>...</Button>
                                {/*) : ''}*/}
                                {/*{console.log(item.resTestBlock.resTestWithScoreList)}*/}
                            </td>
                            <td>{item.totalScore}</td>
                            <td>{item.maxScore}</td>
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
            <Modal size="lg" style={{maxWidth: '1000px', width: '80%'}} isOpen={answersListModal} fade={false}
                   toggle={closeAnswersListModal}>
                <ModalHeader>Savollar ro'yxati</ModalHeader>
                <ModalBody>
                    <Table>
                        <thead>
                        <tr>
                            <th>№</th>
                            <th>Fan</th>
                            <th>Savol</th>
                            <th>Belgilagan javobingiz</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td>
                                {tempQuestionList ? tempQuestionList.map((item, index) =>
                                    <p>{index + 1}</p>
                                ) : ''}

                            </td>
                            <td>
                                {tempQuestionList ? tempQuestionList.map((item, index) =>
                                    /*console.log(item.resTest.resSubject.nameUz, "mana Question ")*/
                                    <p>{item.resTest.resSubject.nameUz}</p>
                                ) : ''}
                            </td>
                            <td>
                                {tempQuestionList ? tempQuestionList.map((item, index) =>
                                    item.resTest.resQuestionList ? item.resTest.resQuestionList.map((item1) =>
                                        /*console.log(item1.question, "mana Question ")*/
                                        <p>{item1.question}</p>
                                    ) : ' '
                                ) : ''}
                            </td>

                            <td>
                                {tempAnswersList ? tempAnswersList.map((item1) =>
                                    /*console.log(item1, "mana question")*/
                                    item1.correct ?
                                        <p className="text-success">{item1.answer}</p>
                                        :
                                        <p className="text-danger">{item1.answer}</p>
                                ) : ''}
                            </td>

                        </tr>
                        </tbody>
                    </Table>
                </ModalBody>
                <ModalFooter>
                    <Button color="primary" onClick={closeAnswersListModal} type="button">Ok</Button>
                </ModalFooter>
            </Modal>
        </div>


    );
};