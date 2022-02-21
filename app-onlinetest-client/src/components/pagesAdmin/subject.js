import React, {useState, useEffect} from "react";
import {
    Button, Col, Container, Modal, NavLink,
    Badge,
    Collapse,
    Navbar,
    NavbarToggler,
    ModalBody, ModalFooter, ModalHeader, Nav, NavItem, Row, Table
} from "reactstrap";
import {AvField, AvForm} from "availity-reactstrap-validation";
import axios from "axios";
import Pagination from "react-js-pagination";

export default function Subject() {


    useEffect(() => {
        if (!stopEffect) {
            axios.get('http://localhost/api/subject/getSubjectByPageable?page=0&size=10', {headers: {"Authorization": localStorage.getItem('OnlineToken')}})
                .then(res => {
                    console.log(res);
                    setSubjectArray(res.data.object);
                    setTotalElement(res.data.totalElements);
                    setStopEffect(true)
                })
        }
        axios.get('http://localhost/api/comment/getViewedCount?viewed=' + false, {headers: {"Authorization": localStorage.getItem('OnlineToken')}})
            .then(res => {
                //console.log(res.data.object,"getViewedCount=============================================");
                setViewedComment(res.data.object);
            });
    }, []);

    const [viewedComment, setViewedComment] = useState(false);
    const [modal, setModal] = useState(false);
    const [addSubjectModal, setAddSubjectModal] = useState(false);
    const [showDeleteModal, setShowDeleteModal] = useState(false);
    const [stopEffect, setStopEffect] = useState(false);
    const [subjectArray, setSubjectArray] = useState([]);
    const [totalElement, setTotalElement] = useState(0);
    const [currentSubject, setCurrentSubject] = useState('');
    const [tempSubjectId, setTempSubjectId] = useState('');
    const [activePage, setActivePage] = useState(1);

    //const [isOpen, setIsOpen] = useState(false);

    const toggle = () => setModal(Modal);

    const addSubject = () => setAddSubjectModal(!addSubjectModal);

    const saveOrEditSubject = (e, v) => {
        if (currentSubject) {
            v = {...v, id: currentSubject.id}
        }
        axios.post('http://localhost/api/subject', v, {headers: {"Authorization": localStorage.getItem('OnlineToken')}})
            .then(res => {
                setModal(!modal);
                axios.get('http://localhost/api/subject/getSubjectByPageable?page=0&size=10', {headers: {"Authorization": localStorage.getItem('OnlineToken')}})
                    .then(res => {
                        console.log(res);
                        setSubjectArray(res.data.object);
                        setTotalElement(res.data.totalElements);
                        setCurrentSubject('');
                    })
                setAddSubjectModal(!addSubjectModal)
            })

    };
    const subjectDeleteYes = () => {
        if (tempSubjectId) {
            axios.delete('http://localhost/api/subject/' + tempSubjectId, {headers: {"Authorization": localStorage.getItem('OnlineToken')}})
                .then(res => {
                    setShowDeleteModal(!showDeleteModal);

                    axios.get('http://localhost/api/subject/getSubjectByPageable?page=0&size=10', {headers: {"Authorization": localStorage.getItem('OnlineToken')}})
                        .then(res => {
                            console.log(res);
                            setSubjectArray(res.data.object);
                            setTotalElement(res.data.totalElements)
                        })
                })
        }
    };
    const editSubject = (item) => {
        setAddSubjectModal(!addSubjectModal);
        setCurrentSubject(item);
        //console.log(item);
    };
    const toggleSubjectSaveOrEditModal = () => {
        setModal(!modal);
        setCurrentSubject('');
    };

    const deleteSubject = (id) => {
        setTempSubjectId(id);
        setShowDeleteModal(!showDeleteModal);
    };

    const toggleSubjectDeleteModal = () => {
        setTempSubjectId([]);
        setShowDeleteModal(!showDeleteModal);
    };
    const handlePageChange = (pageNumber) => {
        setActivePage(pageNumber);
        let page = pageNumber - 1;
        axios.get('http://localhost/api/subject/getSubjectByPageable?page=' + page + '&size=10', {headers: {"Authorization": localStorage.getItem('OnlineToken')}})
            .then(res => {
                console.log(res);
                setSubjectArray(res.data.object);
                setTotalElement(res.data.totalElements);
            })
    };

    return (
        <div>
            <Container>
                <Row>
                    <Col>
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
                    </Col>
                </Row>
                <Row>
                    <Col>
                        <Row>
                            <Button onClick={addSubject}>Add Subject</Button>
                        </Row>
                        <Row>
                            <Table>
                                <thead>
                                <tr>
                                    <th>№</th>
                                    <th>Name UZ</th>
                                    <th>Name RU</th>
                                    <th>Edit | Delete</th>
                                </tr>
                                </thead>
                                <tbody>
                                {subjectArray ? subjectArray.map((item, index) =>
                                    <tr>
                                        <td>{(activePage * 10) + index + 1 - 10}</td>
                                        <td>{item.nameUz}</td>
                                        <td>{item.nameRu}</td>
                                        <td><Button color="info" onClick={() => editSubject(item)}
                                                    className="mr-5">Edit</Button>
                                            <Button color="danger"
                                                    onClick={() => deleteSubject(item.id)}>Delete</Button></td>
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
                    </Col>
                </Row>
            </Container>
            <div>
                <Modal isOpen={addSubjectModal} fade={false} toggle={toggleSubjectSaveOrEditModal}>
                    <ModalHeader>{currentSubject ? "Fanni o'zgartirish " : "Yangi fan qo'shish"}</ModalHeader>
                    <AvForm onValidSubmit={saveOrEditSubject}>
                        <ModalBody>

                            <Row>
                                <Col>
                                    <AvField required={true} type="text"
                                             label="Name uz" className="mt-2"
                                             placeholder="Name uz"
                                             defaultValue={currentSubject ? currentSubject.nameUz : ''}
                                             name="nameUZ"/>
                                    <AvField required={true} type="text"
                                             label="Name ru" placeholder="Name ru"
                                             name="nameRU" defaultValue={currentSubject ? currentSubject.nameRu : ''}/>
                                </Col>
                            </Row>


                        </ModalBody>
                        <ModalFooter>
                            <Button color="danger" onClick={addSubject}>Bekor qilish</Button>
                            <Button color="primary" type="submit">Saqlash</Button>
                        </ModalFooter>
                    </AvForm>
                </Modal>
                <Modal isOpen={showDeleteModal} toggle={toggleSubjectDeleteModal}>
                    <ModalHeader>Subjectni o'chirishni istaysizmi?</ModalHeader>

                    <ModalBody>

                    </ModalBody>
                    <ModalFooter>
                        <Button color="info" onClick={toggleSubjectDeleteModal}>Bekor qilish</Button>
                        <Button className="ml-3" color="danger" onClick={subjectDeleteYes}
                                type="button">O'chirish</Button>
                    </ModalFooter>
                </Modal>
            </div>
        </div>
    );
}
