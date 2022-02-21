import React, {Component} from 'react';
import PropTypes from 'prop-types';
import {
    Badge,
    Button,
    Col,
    Collapse, Modal,
    ModalBody, ModalFooter,
    ModalHeader,
    Nav,
    Navbar,
    NavbarToggler,
    NavItem,
    NavLink,
    Row, Table
} from "reactstrap";
import {AvField, AvForm, AvRadio, AvRadioGroup} from "availity-reactstrap-validation";
import axios from "axios";


class TestBlock extends Component {
    constructor(props) {
        super(props);
        this.state = {
            testBlockArray:[],
            blockArray: [],
            subjectArray: [],
            testWithScoreArray: [{
                subjectId: '',
                testArray: [],
                testId:'',
                score: 0
            }],
            isOpen: false,
            showTestBlockSaveOrEditModal: false,
            currentTestBlock: '',
            level: '',
            currentTest: '',
            totalElement: 0,
            activePage: 1,
            tempTestArray:[],
            tempTestBlockId:'',
            showDeleteTestModal:false,
            showTestsModal:false,
            viewedComment:false
        }
    }

    componentDidMount() {
        axios.get('http://localhost/api/testBlock/getTestBlockByPageable?page=0&size=20', {headers: {"Authorization": localStorage.getItem('OnlineToken')}})
            .then(res => {
                console.log(res,"gettestBlock<<<<<<<<<<<<<<<<<<<<.>>>>>>>>>>>>>>>>>>")
                res.data.object.map(item => {
                    item.resTestWithScoreList.map(item2 => {
                        // console.log(item2.resTest.resSubject.nameUz)

                            // console.log(item2.resTest)

                            // console.log(item.resBlock)


                            // console.log(res.data.object.blockArray,"mana blok Array<<<<<<<<<<<<<<<<<<<<.>>>>>>>>>>>>>>>>>>")
                            // console.log(res.data.object.testWithScoreArray,"mana testlar Array<<<<<<<<<<<<<<<<<<<<.>>>>>>>>>>>>>>>>>>")
                            this.setState({
                                testBlockArray:res.data.object
                                // blockArray:item.resBlock,
                                // testWithScoreArray: [{
                                //     subjectId: item2.resTest.resSubject.id,
                                //     testArray: [],
                                //     score: 0
                                // }]
                                //  blockArray:res.data.object.blockArray,
                                // testWithScoreArray:res.data.testWithScoreArray,
                                // totalElement:res.data.totalElements

                        })
                    })
                })
            });

        axios.get('http://localhost/api/subject/getAll', {headers: {"Authorization": localStorage.getItem('OnlineToken')}})
            .then(res => {
                // console.log(res,"Subjectlar<<<<<<<<<<<<>>>>>>>>>>>");
                this.setState({
                    subjectArray: res.data.object
                });
            });
        axios.get('http://localhost/api/comment/getViewedCount?viewed=' + false, {headers: {"Authorization": localStorage.getItem('OnlineToken')}})
            .then(res => {
                //console.log(res.data.object,"getViewedCount=============================================");
                this.setState({
                    viewedComment:res.data.object
                });
            });
    }

    render() {
        const toggle = () => {
            this.setState({isOpen: !this.state.isOpen})
        };

        const addTestBlock = () => {
            this.setState({showTestBlockSaveOrEditModal: !this.state.showTestBlockSaveOrEditModal})
            this.setState({
                testWithScoreArray: [{
                    subjectId: '',
                    testArray: [],
                    score: 0
                }]
            })
        };
        const getBlockByLevel = (e) => {
            let level = e.target.value;
            this.setState({
                level: level
            });
            axios.get("http://localhost/api/block/getBlockByLevel?level=" + level,
                {headers: {"Authorization": localStorage.getItem('OnlineToken')}})
                .then(res => {
                    console.log(res, "block<<<<<<<<<<>>>>>>>>>>>")
                    this.setState({
                        blockArray: res.data.object
                    })
                })
        };
        const getTestBySubjectAndBlock = (e, index) => {
            let subjectId = e.target.value;
            console.log(subjectId,"SubjectId<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<")
            console.log(index,"SubjectId<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<")
            axios.get("http://localhost/api/test/getTestBySubject?id=" + subjectId + "&level=" + this.state.level,
                {headers: {"Authorization": localStorage.getItem('OnlineToken')}})
                .then(res => {
                    // console.log(res,"test<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>")
                    let s = this.state.testWithScoreArray;
                    s[index].testArray = res.data.object;
                    this.setState({testWithScoreArray: s})
                })
        };
        const deleteTestBlockModal = (id) => {
            this.setState({showDeleteTestModal: !this.state.showDeleteTestModal,tempTestBlockId: id});
        }

        const testCloseModal = ()=>{
            this.setState({showDeleteTestModal: !this.state.showDeleteTestModal});
        }
        const addNewRow = () => {
            let s = this.state.testWithScoreArray;
            s.push({
                subjectId: '',
                testArray: [],
                score: 0
            })
            this.setState({testWithScoreArray: s})
        };
        const deleteRow = (index) => {
            delete this.state.testWithScoreArray[index]

            this.setState(this.state)
        }

        const saveOrEditTestBlock = (e, v) => {
            console.log(v, "ValueSaveOrEdit")
            let tempReqTestWithScores = [];
            Object.keys(v).forEach(inputNames => {
                // console.log(inputNames,"inputNames<<>>>>>><<<<<<<<<<<<>>>>>>>>")
                if (inputNames.split('/')[0] === 'testId') {
                    tempReqTestWithScores[inputNames.split('/')[1]] = {
                        testId: v[inputNames],
                        score: v['score/' + inputNames.split('/')[1]]
                    }
                }
            })
            console.log(tempReqTestWithScores, "PPPPPPPPPPPPPPPPP")
            let reqTestBlock = {
                blockId: v.blockId,
                reqTestWithScores: tempReqTestWithScores
            }
             if (this.state.currentTestBlock){
                reqTestBlock={...reqTestBlock,id:this.state.currentTestBlock.id}
             }
            axios.post('http://localhost/api/testBlock', reqTestBlock, {headers: {"Authorization": localStorage.getItem('OnlineToken')}})
                .then(res => {
                    this.setState({showTestBlockSaveOrEditModal: !this.state.showTestBlockSaveOrEditModal});
                    // axios.get('http://localhost/api/testBlock/getTestBlockByPageable',{headers: {"Authorization": localStorage.getItem('OnlineToken')}})
                    //     .then(res=>{
                    console.log(res, "ressssss");
                    //         // this.setState.testWithScoreArray(res.data.object)
                    //     })
                    axios.get('http://localhost/api/testBlock/getTestBlockByPageable?page=0&size=20', {headers: {"Authorization": localStorage.getItem('OnlineToken')}})
                        .then(res => {
                            console.log(res,"gettestBlock<<<<<<<<<<<<<<<<<<<<.>>>>>>>>>>>>>>>>>>")
                            res.data.object.map(item => {
                                item.resTestWithScoreList.map(item2 => {
                                    // console.log(item2.resTest.resSubject.nameUz)

                                    // console.log(item2.resTest)

                                    // console.log(item.resBlock)


                                    // console.log(res.data.object.blockArray,"mana blok Array<<<<<<<<<<<<<<<<<<<<.>>>>>>>>>>>>>>>>>>")
                                    // console.log(res.data.object.testWithScoreArray,"mana testlar Array<<<<<<<<<<<<<<<<<<<<.>>>>>>>>>>>>>>>>>>")
                                    this.setState({
                                        testBlockArray:res.data.object
                                        // blockArray:item.resBlock,
                                        // testWithScoreArray: [{
                                        //     subjectId: item2.resTest.resSubject.id,
                                        //     testArray: [],
                                        //     score: 0
                                        // }]
                                        //  blockArray:res.data.object.blockArray,
                                        // testWithScoreArray:res.data.testWithScoreArray,
                                        // totalElement:res.data.totalElements

                                    })
                                })
                            })
                        });
                    this.setState({
                        testWithScoreArray:[]
                    })
                })

        };
        const editTestBlock=(item)=>{
            let tempTestBlockArray=[];
            let tempTestWithScoreArray=[];
            if (item.resTestWithScoreList){
                let s=item.resTestWithScoreList
                let interator = s.values();
                for (let value of interator){
                    console.log(value);
                    tempTestWithScoreArray.push({
                        subjectId: value.resTest.resSubject.id,
                        testArray: value.resTestListBySubjectAndLevel,
                        testId:value.resTest.id,
                        score: value.score
                    })
                }
                this.setState({
                    testWithScoreArray:tempTestWithScoreArray
                })

            }

            this.setState({
                showTestBlockSaveOrEditModal:!this.state.showTestBlockSaveOrEditModal,
                currentTestBlock:item,
                blockArray:item.resBlockListByLevel,
                level:item.resBlock.level
            })


           // console.log(item.resTestWithScoreList)
            // console.log(item)
        };
        const deleteTestBlock=()=>{
            axios.delete('http://localhost/api/testBlock/'+this.state.tempTestBlockId,{headers: {"Authorization": localStorage.getItem('OnlineToken')}})
                .then(res=>{
                    axios.get('http://localhost/api/testBlock/getTestBlockByPageable?page=0&size=20', {headers: {"Authorization": localStorage.getItem('OnlineToken')}})
                        .then(ress => {
                            console.log(ress,"gettestBlock<<<<<<<<<<<<<<<<<<<<.>>>>>>>>>>>>>>>>>>")
                            this.setState({showDeleteTestModal: !this.state.showDeleteTestModal,
                                testBlockArray:ress.data.object
                            })

                        });
                })
        };

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
                                        <NavLink href="/comment">Comment <Badge color="primary"
                                                                                pill>{this.state.viewedComment ? this.state.viewedComment: ''}</Badge></NavLink>
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
                        <h1>Testblock CRUD</h1>
                    </Col>
                </Row>
                <Row>
                    <Button className="mr-2" onClick={addTestBlock}>Add TestBlock</Button>
                </Row>
                <Row>
                    <Table>
                        <thead>
                        <tr>
                            <th>№</th>
                            <th>Daraja</th>
                            <th>Blok</th>
                            <th>Testlar</th>
                            <th>Edit | Delete</th>
                        </tr>
                        </thead>
                        {/*{console.log(this.state.blockArray)}*/}
                        <tbody>
                        {this.state.testBlockArray ? this.state.testBlockArray.map((item, index) =>
                            <tr>
                                <td>{(this.state.activePage * 10) + index + 1 - 10}</td>
                                <td>{item.resBlock.level}</td>
                                <td>{item.resBlock.nameUz}</td>
                                <td>
                                    {item.resTestWithScoreList ? item.resTestWithScoreList.map((item2, index2) =>

                                        <p className="my-0 justify-content-center">
                                            <b>{item2.resTest.resSubject.nameUz} : {item2.score}</b></p>
                                    ) : ''}

                                </td>
                                <td><Button color="info" onClick={() => editTestBlock(item)}
                                            className="mr-5">Edit</Button>
                                    <Button color="danger" onClick={() => deleteTestBlockModal(item.id)}>Delete</Button></td>


                                {/*<td>{console.log(item2.resTestWithScoreList,"mana blok arrey table digisi")}</td>*/}


                            </tr>
                        ) : ''}
                        </tbody>
                    </Table>
                </Row>
                <Modal size="lg" style={{maxWidth: '1600px', width: '80%'}}
                       isOpen={this.state.showTestBlockSaveOrEditModal} fade={false}
                       toggle={addTestBlock}>
                    <ModalHeader>Test Block Modal</ModalHeader>
                    <AvForm onValidSubmit={saveOrEditTestBlock}>
                        <ModalBody>
                            <Row>
                                <Col>
                                    <AvField type="select" onChange={getBlockByLevel}
                                             className="mt-2" label="Qiyinlik darajasi"
                                             defaultValue={this.state.currentTestBlock ? this.state.currentTestBlock.resBlock.level : ' '}
                                             placeholder="Darajani tanlang"
                                             name="level">
                                        <option>Darajani tanlang</option>
                                        <option value="EASY">Oson</option>
                                        <option value="MEDIUM">O'rtacha</option>
                                        <option value="HARD">Qiyin</option>
                                    </AvField>
                                </Col>
                                <Col>
                                    <AvField type="select"
                                             className="mt-2"
                                             label="Blok"
                                             placeholder="Blokni tanlang"
                                             defaultValue={this.state.currentTestBlock ? this.state.currentTestBlock.resBlock.id : ''}
                                             name="blockId">
                                        <option>Blokni tanlang</option>
                                        {this.state.blockArray ? this.state.blockArray.map(item =>
                                            <option value={item.id}>{item.nameUz}</option>
                                        ) : ''}
                                    </AvField>
                                </Col>
                            </Row>
                            {this.state.testWithScoreArray ? this.state.testWithScoreArray.map((item, index) =>
                                <Row>
                                    <Col>
                                        <AvField type="select" onChange={(e) => getTestBySubjectAndBlock(e, index)}
                                                 defaultValue={item.subjectId?item.subjectId:''}
                                                 className="mt-2" label="Fan" placeholder="Fanni tanlang" name="testId">
                                            <option>Fanni tanlang</option>
                                            {this.state.subjectArray ? this.state.subjectArray.map(item =>
                                                <option value={item.id}>{item.nameUz}</option>
                                            ) : ''}
                                        </AvField>
                                    </Col>
                                    <Col>
                                        <AvField type="select"
                                                 className="mt-2" label="Test" placeholder="Testni tanlang"
                                                 defaultValue={item.testId?item.testId:''}
                                                 name={`testId/${index}`}>
                                            <option>Testni tanlang</option>
                                            {item.testArray ? item.testArray.map((item2, ind) =>
                                                <option value={item2.id}>{item2.title}</option>
                                            ) : ''}
                                        </AvField>
                                    </Col>
                                    <Col>
                                        <AvField required={true} type="number"
                                                 className="mt-2"
                                                 defaultValue={item.score?item.score:''}
                                                 label="Ball"
                                                 placeholder="Ball"
                                                 name={`score/${index}`}
                                        />
                                    </Col>
                                    <Col>
                                        <Button type="button" onClick={() => deleteRow(index)} className="mt-4"
                                                color="danger" style={{borderRadius: "20px"}}
                                        >-</Button>
                                    </Col>
                                </Row>
                            ) : ''}
                            <Row>
                                <Col md={{size: 2, offset: 5}}>
                                    <Button type="button" className="ml-5" color="info" style={{borderRadius: "20px"}}
                                            onClick={addNewRow}>+</Button>
                                </Col>

                            </Row>

                        </ModalBody>
                        <ModalFooter><Button color="danger" onClick={addTestBlock}>Bekor qilish</Button>
                            <Button color="primary" type="submit">Saqlash</Button>
                        </ModalFooter>
                    </AvForm>
                </Modal>
                <Modal isOpen={this.state.showDeleteTestModal} toggle={testCloseModal}>
                    <ModalHeader>Rostan xam testni o'chirishni istaysizmi?</ModalHeader>


                    <ModalBody>

                    </ModalBody>
                    <ModalFooter>
                        <Button color="danger" onClick={testCloseModal}>Bekor qilish</Button>
                        <Button className="ml-3" color="success" onClick={deleteTestBlock}
                                type="button">O'chirish</Button>
                    </ModalFooter>

                </Modal>
            </div>
        );
    }
}

TestBlock.propTypes = {};

export default TestBlock;
