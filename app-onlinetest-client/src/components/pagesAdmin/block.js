import React, {useEffect, useState} from "react";
import {
    Button,
    Col,
    Badge,
    Collapse,
    Modal, ModalBody, ModalFooter,
    ModalHeader,
    Nav,
    Navbar,
    NavbarToggler,
    NavItem,
    NavLink,
    Row,
    Table
} from "reactstrap";
import {AvField, AvForm} from "availity-reactstrap-validation";
import axios from "axios";
import Pagination from "react-js-pagination";

export default function Block() {
    useEffect(() => {
        if (!stopEffect) {
            axios.get('http://localhost/api/block/getBlockByPageble?page=0&size=10', {headers: {"Authorization": localStorage.getItem('OnlineToken')}})
                .then(res => {
                    console.log(res);
                    setBlockArray(res.data.object);
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

    const [viewedComment,setViewedComment] = useState(false);
    const [isOpen, setIsOpen] = useState(false);
    const [addBlockModal, setAddBlockModal] = useState(false);
    const [blockArray, setBlockArray] = useState([]);
    const [totalElement, setTotalElement] = useState(0);
    const [modal, setModal] = useState(false);
    const [showDeleteModal, setShowDeleteModal] = useState(false);
    const [stopEffect, setStopEffect] = useState(false);
    const [currentBlock, setCurrentBlock] = useState('');
    const [tempBlockId, setTempBlockId] = useState('');
    const [activePage, setActivePage] = useState(1);

    const editBlock = (item) => {
        setAddBlockModal(!addBlockModal);
        setCurrentBlock(item);
        //console.log(item);
    };

    const addBlock = () => {
        setAddBlockModal(!addBlockModal);
        setCurrentBlock('');
    };
    const handlePageChange = (pageNumber) => {
        setActivePage(pageNumber);
        let page = pageNumber - 1;
        axios.get('http://localhost/api/block/getBlockByPageble?page=' + page + '&size=10', {headers: {"Authorization": localStorage.getItem('OnlineToken')}})
            .then(res => {
                console.log(res);
                setBlockArray(res.data.object);
                setTotalElement(res.data.totalElements);
            })
    };
    console.log(blockArray);
    const saveOrEditBlock = (e, v) => {
        console.log(v, "<<<<<<<<<<<<>>>>>>>>>>>>")
        if (currentBlock) {
            v = {...v, id: currentBlock.id}
        }
        axios.post('http://localhost/api/block', v, {headers: {"Authorization": localStorage.getItem('OnlineToken')}})
            .then(res => {
                setModal(!modal);
                axios.get('http://localhost/api/block/getBlockByPageble?page=0&size=10', {headers: {"Authorization": localStorage.getItem('OnlineToken')}})
                    .then(res => {
                        console.log(res);
                        setBlockArray(res.data.object);
                        setTotalElement(res.data.totalElements);
                        setCurrentBlock('');
                    })
                setAddBlockModal(!addBlockModal);
            })

    };
    const blockDeleteYes = () => {
        if (tempBlockId) {
            axios.delete('http://localhost/api/block/' + tempBlockId, {headers: {"Authorization": localStorage.getItem('OnlineToken')}})
                .then(res => {
                    setShowDeleteModal(!showDeleteModal);

                    axios.get('http://localhost/api/block/getBlockByPageble?page=0&size=10', {headers: {"Authorization": localStorage.getItem('OnlineToken')}})
                        .then(res => {
                            console.log(res);
                            setBlockArray(res.data.object);
                            setTotalElement(res.data.totalElements)
                        })
                })
        }
    };

    const deleteBlock = (id) => {
        setTempBlockId(id);
        setShowDeleteModal(!showDeleteModal);
    };

    const toggleBlockDeleteModal = () => {
        setTempBlockId('');
        setShowDeleteModal(!showDeleteModal);
    };
    const toggle = () => setIsOpen(!isOpen);
    return (
        <div>
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
                                    <NavLink href="/comment">Comment <Badge color="primary" pill>{viewedComment?viewedComment : ''}</Badge></NavLink>
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
                    <h1>Block CRUD</h1>
                </Col>
            </Row>
            <Row>
                <Button onClick={addBlock}>Add Block</Button>
            </Row>
            <Row>
                <Table>
                    <thead>
                    <tr>
                        <th>№</th>
                        <th>Name UZ</th>
                        <th>Name RU</th>
                        <th>Level</th>
                        <th>Edit | Delete</th>
                    </tr>
                    </thead>
                    <tbody>
                    {blockArray ? blockArray.map((item, index) =>
                        <tr>
                            <td>{(activePage * 10) + index + 1 - 10}</td>
                            <td>{item.nameUz}</td>
                            <td>{item.nameRu}</td>
                            <td>{item.level}</td>
                            <td><Button color="info" onClick={() => editBlock(item)}
                                        className="mr-5">Edit</Button>
                                <Button color="danger" onClick={() => deleteBlock(item.id)}>Delete</Button></td>
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
            <div>
                <Modal isOpen={addBlockModal} fade={false} toggle={addBlock}>
                    <ModalHeader>{currentBlock ? "Block o'zgartirish " : "Yangi Block qo'shish"}</ModalHeader>
                    <AvForm onValidSubmit={saveOrEditBlock}>
                        <ModalBody>

                            <Row>
                                <Col>
                                    <AvField required={true} type="text"
                                             label="Name uz" className="mt-2"
                                             placeholder="Name uz"
                                             defaultValue={currentBlock ? currentBlock.nameUz : ''}
                                             name="nameUz"/>
                                    <AvField required={true} type="text"
                                             label="Name ru" placeholder="Name ru"
                                             name="nameRu" defaultValue={currentBlock ? currentBlock.nameRu : ''}/>
                                    <AvField type="select" defaultValue={currentBlock ? currentBlock.level : ''}
                                             className="mt-2" placeholder="Darajani tanlang" name="level">
                                        <option>Darajani tanlang</option>
                                        <option value="EASY">Oson</option>
                                        <option value="MEDIUM">O'rtacha</option>
                                        <option value="HARD">Qiyin</option>

                                    </AvField>
                                </Col>
                            </Row>


                        </ModalBody>
                        <ModalFooter>
                            <Button color="danger" onClick={addBlock}>Bekor qilish</Button>
                            <Button color="primary" type="submit">Saqlash</Button>
                        </ModalFooter>
                    </AvForm>
                </Modal>
                <Modal isOpen={showDeleteModal} toggle={toggleBlockDeleteModal}>
                    <ModalHeader>Subjectni o'chirishni istaysizmi?</ModalHeader>

                    <ModalBody>

                    </ModalBody>
                    <ModalFooter>
                        <Button color="danger" onClick={toggleBlockDeleteModal}>Bekor qilish</Button>
                        <Button className="ml-3" color="success" onClick={blockDeleteYes}
                                type="button">O'chirish</Button>
                    </ModalFooter>
                </Modal>
            </div>
        </div>
    )
}