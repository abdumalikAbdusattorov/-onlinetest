import React, {Component, useState} from 'react';
import PropTypes from 'prop-types';
import {Modal, ModalBody, ModalFooter, ModalHeader, Navbar} from "reactstrap";
import {AvFeedback, AvField, AvForm, AvGroup, AvInput, AvRadio, AvRadioGroup} from "availity-reactstrap-validation";
import {Link} from "react-router-dom";
import {
    Button, Card, CardBody, CardImg, CardSubtitle, CardText, CardTitle,
    Col,
    Collapse,
    Nav,
    NavbarToggler,
    NavItem,
    NavLink,
    Row,
    Table
} from "reactstrap";
import axios from "axios";
import {MdFeaturedPlayList} from 'react-icons/md';

class SolveTest extends Component {

    constructor(props) {

        super(props);
        this.state = {
            resTestWithScoreList: [],
            solveTestTo: false,
            blockTestCardArray: [],
            blockArray: [],
            subjectArray: [],
            testBlockArray: [],
            questionListArray: [],
            testWithScoreArray: [{
                subjectId: '',
                testArray: [],
                score: 0
            }],
            item: '',
            isOpen: false,
            showTestBlockSaveOrEditModal: false,
            currentTestBlock: '',
            level: '',
            currentTest: '',
            testBlock: '',
            blockId: '',
            questionCommentModal: false,
            questionCommentId: ''

        }

    }


    render() {
        const getTestBlocks = (e) => {
            let blockId = e.target.value;
            axios.get('http://localhost/api/testBlock/getByLevelAndBlock?level=' + this.state.level + '&blockId=' + blockId, {headers: {"Authorization": localStorage.getItem('OnlineToken')}})
                .then(res => {
                    console.log(res.data.object, "Code<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>");
                    this.setState({
                        blockTestCardArray: res.data.object
                    })
                })
        };


        const toggle1 = () => {
            this.setState({isOpen: !this.state.isOpen})
        };
        const getBlockByLevel = (e) => {
            let level = e.target.value;
            this.setState({
                level: level
            });
            axios.get("http://localhost/api/block/getBlockByLevel?level=" + level,
                {headers: {"Authorization": localStorage.getItem('OnlineToken')}})
                .then(res => {
                    console.log(res, "block<<<<<<<<<<>>>>>>>>>>>");
                    this.setState({
                        blockArray: res.data.object
                    })
                })
        };

        const sendTest = (e, v) => {
            let answerList = [];
            Object.keys(v).forEach(inputName => {
                //console.log(inputName,"Qara")
                if (inputName.split("/")[0] === 'answerRadio') {
                    answerList.push({
                        answerId:v[inputName].split("/")[1],
                        score:v[inputName].split("/")[2],
                        testBlockId:v[inputName].split("/")[3]
                    })
                }
            });
            axios.post("http://localhost/api/test/solveTest",
                answerList,{headers: {"Authorization": localStorage.getItem('OnlineToken')}})
                .then(res =>{
                    console.log(res,"Ana")
                })

            //console.log(answerList,"Ana")
        };

        const saveComment = (e, v) => {
            v = {questionId: this.state.questionCommentId, ...v};
            //console.log(v,"comment text");
            axios.post('http://localhost/api/comment', v, {headers: {"Authorization": localStorage.getItem('OnlineToken')}})
                .then(res => {
                    console.log(res);
                    this.setState({
                        questionCommentModal: false,
                        questionCommentId: ''
                    });
                })
        };


        const questionComment = (id) => {
            //console.log(id,"question iddddddddddddddddddddddddd")

            this.setState({
                questionCommentModal: true,
                questionCommentId: id
            })
        };

        const toggleSubjectDeleteModal = () => {
            this.setState({
                questionCommentModal: false,
                questionCommentId: ''
            })
        };

        const solveTest1 = (item) => {
            console.log(item);
            this.setState({solveTestTo: !this.state.solveTestTo, item: item})
        };
        const myStyle = {
            height: "30px",
            borderStyle: "none",
            background: "none",
            textAlign: "center",
            color: "black"
        };

        return (
            <div>
                <Navbar color="light" light expand="md">
                    <NavbarToggler onClick={toggle1}/>
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
                {this.state.solveTestTo ?
                    <div>
                        {this.state.item ?
                            <AvForm onValidSubmit={sendTest}>
                                {this.state.item.resTestWithScoreList ? this.state.item.resTestWithScoreList.map((item2, index2) =>
                                    <div key={index2}>
                                        {item2.resTest ?
                                            <div>
                                                <h4 className="my-4 text-center">{item2.resTest.resSubject.nameUz}</h4>
                                                {item2.resTest.resQuestionList ? item2.resTest.resQuestionList.map((item3, index3) =>
                                                    <Row key={index2 + index3}>
                                                        <Col>{(index3 + 1) + "." + item3.question}
                                                            <AvRadioGroup key={`question/${index2}/${index3}`}
                                                                          name={`answerRadio/${index2}/${index3}`}
                                                                          required>
                                                                <Row key={index2 + index3}>
                                                                    {item3.resAnswers ? item3.resAnswers.map((item4, index4) =>
                                                                        <Col className="d-flex">
                                                                            <AvRadio
                                                                                key={`answerRadio/${index3 + index4}`}
                                                                                className=" m-0,p-0"
                                                                                style={{display: "inline!important"}}
                                                                                customInput
                                                                                value={`answerRadio/${item4.id}/${item2.score}/${this.state.item.id}`}/>
                                                                            {item4.answer}
                                                                        </Col>
                                                                    ) : ''}
                                                                </Row>

                                                            </AvRadioGroup>
                                                        </Col>
                                                        <button type="button" style={myStyle}
                                                                onClick={() => questionComment(item3.id)}>
                                                            <MdFeaturedPlayList/></button>
                                                    </Row>
                                                ) : ''
                                                }

                                            </div>
                                            : ''}
                                    </div>
                                ) : ''}
                                <Row>
                                    <Col>
                                        <button className="btn btn-danger" type="submit">Jo'natish</button>
                                    </Col>
                                </Row>
                            </AvForm>
                            : ''}

                    </div>
                    : <div>
                        <AvForm>
                            <Row>
                                <Col>
                                    <AvField type="select" onChange={getBlockByLevel}
                                             className="mt-2" label="Qiyinlik darajasi" placeholder="Darajani tanlang"
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
                                             onChange={getTestBlocks}
                                             placeholder="Blokni tanlang"
                                             name="blockId">

                                        <option>Blokni tanlang</option>
                                        {this.state.blockArray ? this.state.blockArray.map(item =>
                                            <option value={item.id}>{item.nameUz}</option>
                                        ) : ''}
                                    </AvField>
                                </Col>
                            </Row>
                        </AvForm>
                        {this.state.blockTestCardArray ? this.state.blockTestCardArray.map((item, index) =>
                            <Row>
                                <Col xs="3">
                                    <Card className="w-auto">
                                        <CardBody>
                                            <CardTitle><h5>{item.resBlock.nameUz} : {item.resBlock.level}</h5>
                                            </CardTitle>
                                            {item.resTestWithScoreList ? item.resTestWithScoreList.map((item2, index2) =>
                                                <CardSubtitle>{(index2 + 1) + "." + item2.resTest.resSubject.nameUz} : {item2.score}</CardSubtitle>
                                            ) : ''}
                                            <Button type="button" onClick={() => solveTest1(item)}>Test
                                                ishlash</Button>

                                        </CardBody>
                                    </Card>
                                </Col>
                            </Row>
                        ) : ''}
                    </div>}

                <Modal isOpen={this.state.questionCommentModal} toggle={toggleSubjectDeleteModal}>
                    <ModalHeader>Xatolik xaqida xabar berish</ModalHeader>
                    <AvForm onValidSubmit={saveComment}>
                        <ModalBody>
                            <Row>
                                <Col>
                                    <AvField required={true} type="text"
                                             label="Izoh" className="mt-2"
                                             placeholder="Yozing"
                                             name="commentText"/>
                                </Col>
                            </Row>
                        </ModalBody>

                        <ModalFooter>
                            <Button color="info" onClick={toggleSubjectDeleteModal}>Bekor qilish</Button>
                            <Button color="primary" type="submit">Jo'natish</Button>
                        </ModalFooter>
                    </AvForm>
                </Modal>

            </div>
        );
    }
}

SolveTest.propTypes = {};

export default SolveTest;