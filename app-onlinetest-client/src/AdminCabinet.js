import React, {useState, useEffect} from 'react';
import {
    Badge,
    Button,
    Col,
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
import axios from "axios";
import {AvField, AvForm, AvRadio, AvRadioGroup} from "availity-reactstrap-validation";
import Pagination from "react-js-pagination";

export default function AdminCabinet() {
    useEffect(() = > {
        if(
    !stopEffect
)
    {
        axios.get('http://localhost/api/test/getTestsByPageable?page=0&size=10', {headers: {"Authorization": localStorage.getItem('OnlineToken')}})
            .then(res = > {
            console.log(res);
        setTestArray(res.data.object);
        setTotalElement(res.data.totalElements);


    })
        ;
        axios.get('http://localhost/api/subject/getAll', {headers: {"Authorization": localStorage.getItem('OnlineToken')}})
            .then(res = > {
            console.log(res);
        setSubjectArray(res.data.object);

    })
        ;
        setStopEffect(true);
    }
    axios.get('http://localhost/api/comment/getViewedCount?viewed=' + false, {headers: {"Authorization": localStorage.getItem('OnlineToken')}})
        .then(res = > {
        //console.log(res.data.object,"getViewedCount=============================================");
        setViewedComment(res.data.object
)
    ;
})
    ;
},
    []
)
    ;

    const [viewedComment, setViewedComment] = useState(false);
    const [isOpen, setIsOpen] = useState(false);
    const [stopEffect, setStopEffect] = useState(false);
    const [testArray, setTestArray] = useState([]);
    const [subjectArray, setSubjectArray] = useState([]);
    const [totalElement, setTotalElement] = useState(0);
    const [addTestModal, setAddTestModal] = useState(false);
    const [questionListModal, setQuestionListModal] = useState(false);
    const [currentTest, setCurrentTest] = useState('');
    const [showDeleteModal, setShowDeleteModal] = useState(false);
    const [modal, setModal] = useState(false);
    const [activePage, setActivePage] = useState(1);
    const [tempTestId, setTempTestId] = useState('');
    const [reqQuestionList, setReqQuestionList] = useState([
        {
            question: '',
            correctAnswerr: '',
            reqAnswerList: [
                {
                    answer: '',
                    correctAnswer: false
                },
                {
                    answer: '',
                    correctAnswer: false
                },
                {
                    answer: '',
                    correctAnswer: false
                },
                {
                    answer: '',
                    correctAnswer: false
                }
            ]
        }
    ]);
    const [tempQuestionList, setTempQuestionList] = useState([]);
    const addTest = () =
>
    {
        setAddTestModal(!addTestModal);
        setCurrentTest('');
        setReqQuestionList([{
            question: '',
            reqAnswerList: [
                {
                    answer: '',
                    correctAnswer: false
                },
                {
                    answer: '',
                    correctAnswer: false
                },
                {
                    answer: '',
                    correctAnswer: false
                },
                {
                    answer: '',
                    correctAnswer: false
                }
            ]
        }])
    }
    ;
    const handlePageChange = (pageNumber) =
>
    {
        setActivePage(pageNumber);
        let page = pageNumber - 1;
        axios.get('http://localhost/api/test/getTestsByPageable?page=' + page + '&size=10', {headers: {"Authorization": localStorage.getItem('OnlineToken')}})
            .then(res = > {
            console.log(res);
        setTestArray(res.data.object);
        setTotalElement(res.data.totalElements);
    })
    }
    ;
    const deleteTest = (id) =
>
    {
        setTempTestId(id);
        setShowDeleteModal(!showDeleteModal);
    }
    ;
    const editTest = (item) =
>
    {
        let tempTestQuestionArray = [];
        setAddTestModal(!addTestModal);
        setCurrentTest(item);
        console.log(item);
        item.resQuestionList.map((item2, ind) = > {
            let s = {
                question: item2.question,
                correctAnswerr: '',
                reqAnswerList: item2.resAnswers
            };
        item2.resAnswers.map((item3, index) = > {
            if(item3.correct
    )
        {
            s.correctAnswerr = 'answerRadio/' + ind + '/' + index
        }
    })
        ;
        tempTestQuestionArray.push(s);

    })
        ;
        setReqQuestionList(tempTestQuestionArray);
    }
    ;
    const saveOrEditTest = (e, v) =
>
    {
        let questionList = [];
        Object.keys(v).forEach(inputName = > {
            // console.log(inputName, "inputNmae<<<<<<<<<<<>>>>>>>>>>>>>>");
            if(inputName.split('/')[0] === 'question'
    )
        {
            questionList[inputName.split('/')[1]] = {
                question: v[inputName],
                reqAnswerList: []
            }
        }
        if (inputName.split('/')[0] === 'answerRadio') {
            let s = v[inputName];

            console.log(s, "SSSSSSSSS");
            if (s.split('/')[2] === '0') {
                questionList[s.split('/')[1]].reqAnswerList = [
                    {
                        answer: reqQuestionList[s.split('/')[1]].reqAnswerList[0].answer,
                        correctAnswer: true
                    },
                    {
                        answer: reqQuestionList[s.split('/')[1]].reqAnswerList[1].answer,
                        correctAnswer: false
                    },
                    {
                        answer: reqQuestionList[s.split('/')[1]].reqAnswerList[2].answer,
                        correctAnswer: false
                    },
                    {
                        answer: reqQuestionList[s.split('/')[1]].reqAnswerList[3].answer,
                        correctAnswer: false
                    }
                ]
            }
            if (s.split('/')[2] === '1') {
                questionList[s.split('/')[1]].reqAnswerList = [
                    {
                        answer: reqQuestionList[s.split('/')[1]].reqAnswerList[0].answer,
                        correctAnswer: false
                    },
                    {
                        answer: reqQuestionList[s.split('/')[1]].reqAnswerList[1].answer,
                        correctAnswer: true
                    },
                    {
                        answer: reqQuestionList[s.split('/')[1]].reqAnswerList[2].answer,
                        correctAnswer: false
                    },
                    {
                        answer: reqQuestionList[s.split('/')[1]].reqAnswerList[3].answer,
                        correctAnswer: false
                    }
                ]
            }
            if (s.split('/')[2] === '2') {
                questionList[s.split('/')[1]].reqAnswerList = [
                    {
                        answer: reqQuestionList[s.split('/')[1]].reqAnswerList[0].answer,
                        correctAnswer: false
                    },
                    {
                        answer: reqQuestionList[s.split('/')[1]].reqAnswerList[1].answer,
                        correctAnswer: false
                    },
                    {
                        answer: reqQuestionList[s.split('/')[1]].reqAnswerList[2].answer,
                        correctAnswer: true
                    },
                    {
                        answer: reqQuestionList[s.split('/')[1]].reqAnswerList[3].answer,
                        correctAnswer: false
                    }
                ]
            }
            if (s.split('/')[2] === '3') {
                questionList[s.split('/')[1]].reqAnswerList = [
                    {
                        answer: reqQuestionList[s.split('/')[1]].reqAnswerList[0].answer,
                        correctAnswer: false
                    },
                    {
                        answer: reqQuestionList[s.split('/')[1]].reqAnswerList[1].answer,
                        correctAnswer: false
                    },
                    {
                        answer: reqQuestionList[s.split('/')[1]].reqAnswerList[2].answer,
                        correctAnswer: false
                    },
                    {
                        answer: reqQuestionList[s.split('/')[1]].reqAnswerList[3].answer,
                        correctAnswer: true
                    }
                ]
            }


        }

    })
        ;
        let reqTest = {
            title: v.title,
            level: v.level,
            subjectId: v.subjectId,
            reqQuestionList: questionList
        };
        if (currentTest) {
            reqTest = {...reqTest, id: currentTest.id}
        }
        axios.post('http://localhost/api/test', reqTest, {headers: {"Authorization": localStorage.getItem('OnlineToken')}})
            .then(res = > {
            setModal(
        !modal
    )
        ;
        axios.get('http://localhost/api/test/getTestsByPageable?page=0&size=10', {headers: {"Authorization": localStorage.getItem('OnlineToken')}})
            .then(res = > {
            console.log(res);
        setTestArray(res.data.object);
        setTotalElement(res.data.totalElements);
        setCurrentTest('');
    })
        setAddTestModal(!addTestModal);
    })

    }
    ;

    const toggleCloseTestModal = () =
>
    {
        setShowDeleteModal(!showDeleteModal);

    }

    const addNewRow = () =
>
    {
        setReqQuestionList([...reqQuestionList, {
            question: '',
            reqAnswerList: [
                {
                    answer: '',
                    correctAnswer: false
                },
                {
                    answer: '',
                    correctAnswer: false
                },
                {
                    answer: '',
                    correctAnswer: false
                },
                {
                    answer: '',
                    correctAnswer: false
                }
            ]
        }
    ])
        ;
        console.log(reqQuestionList, "Savollar=========")
    }
    ;

    const toggle = () =
>
    setIsOpen(!isOpen);


    const testDeleteYes = () =
>
    {
        axios.delete('http://localhost/api/test/' + tempTestId, {headers: {"Authorization": localStorage.getItem('OnlineToken')}})
            .then(res = > {
            setModal(
        !modal
    )
        ;
        axios.get('http://localhost/api/test/getTestsByPageable?page=0&size=10', {headers: {"Authorization": localStorage.getItem('OnlineToken')}})
            .then(res = > {
            console.log(res);
        setTestArray(res.data.object);
        setTotalElement(res.data.totalElements);
        setCurrentTest('');
    })
        setShowDeleteModal(!showDeleteModal);
        axios.get('http://localhost/api/comment/getViewedCount?viewed=' + false, {headers: {"Authorization": localStorage.getItem('OnlineToken')}})
            .then(res = > {
            //console.log(res.data.object,"getViewedCount=============================================");
            setViewedComment(res.data.object
    )
        ;
    })
        ;
    })


    }
    ;

    const getXQuestionYAnswer = (e, ind, index) =
>
    {
        console.log(e.target.value, "VAL");
        let tempArray = reqQuestionList;
        tempArray[ind].reqAnswerList[index].answer = e.target.value;
        setReqQuestionList(tempArray);
        console.log(reqQuestionList, "QuestionList")
    }
    ;

    const showTestQuestions = (questionList) =
>
    {
        console.log(tempQuestionList, "before");
        console.log(questionList, "questionList");
        setTempQuestionList(questionList);
        setQuestionListModal(!questionListModal);
    }
    ;
    const closeQuestionListModal = () =
>
    setQuestionListModal(!questionListModal);

    console.log(tempQuestionList, "after");

    return (
        < div >
        < Row >
        < Col >
        < Navbar
    color = "light"
    light
    expand = "md" >
        < NavbarToggler
    onClick = {toggle}
    />
    < Collapse
    navbar >
    < Nav
    className = "mr-auto"
    navbar >
    < NavItem >
    < NavLink
    href = "/adminCabinet" > Test < /NavLink>
        < /NavItem>
        < NavItem >
        < NavLink
    href = "/block" > Block < /NavLink>
        < /NavItem>
        < NavItem >
        < NavLink
    href = "/testBlock" > TestBlock < /NavLink>
        < /NavItem>
        < NavItem >
        < NavLink
    href = "/historyUser" > HistoryUser < /NavLink>
        < /NavItem>
        < NavItem >
        < NavLink
    href = "/comment" > Comment < Badge
    color = "primary"
    pill > {viewedComment? viewedComment : ''} < /Badge> </
    NavLink >
    < /NavItem>
    < NavItem >
    < NavLink
    href = "/subject" > Subject < /NavLink>
        < /NavItem>
        < NavItem >
        < NavLink
    href = "/" > Chiqish < /NavLink>
        < /NavItem>
        < /Nav>
        < /Collapse>
        < /Navbar>
        < /Col>
        < /Row>
        < Row >
        < Col >
        < h1 > Test
    CRUD < /h1>
    < /Col>
    < /Row>
    < Row >
    < Button
    className = "mr-2"
    onClick = {addTest} > Add
    Test < /Button>
    < /Row>
    < Row >
    < Table >
    < thead >
    < tr >
    < th >№<
    /th>
    < th > Title < /th>
    < th > Level < /th>
    < th > Subject < /th>
    < th > Questions < /th>
    < th > Edit | Delete < /th>
    < /tr>
    < /thead>
    < tbody >
    {
        testArray ? testArray.map((item, index) = >
            < tr >
            < td > {(activePage * 10) + index + 1 - 10
    } < /td>
    < td > {item.title} < /td>
    < td > {item.level} < /td>
    < td > {item.resSubject.nameUz} < /td>

    < td > < Button
    color = "primary"
    onClick = {() =
>
    showTestQuestions(item.resQuestionList)
}
    className = "mr-5" >
...<
    /Button></
    td >
    < td > < Button
    color = "info"
    onClick = {() =
>
    editTest(item)
}
    className = "mr-5" > Edit < /Button>
        < Button
    color = "danger"
    onClick = {() =
>
    deleteTest(item.id)
}>
    Delete < /Button></
    td >
    < /tr>
) :
    ''
}
<
    /tbody>
    < /Table>
    < /Row>
    < Row >
    < Col >
    < Pagination
    activePage = {activePage}
    itemsCountPerPage = {10}
    totalItemsCount = {totalElement}
    pageRangeDisplayed = {5}
    onChange = {handlePageChange.bind(this)}
    itemClass = "page-item"
    linkClass = "page-link"
        / >
        < /Col>
        < /Row>
        < div >
        < Modal
    size = "lg"
    style = {
    {
        maxWidth: '1600px', width
    :
        '80%'
    }
}
    isOpen = {addTestModal}
    fade = {false}
    toggle = {addTest} >
        < ModalHeader > {currentTest ? "Test o'zgartirish " : "Yangi Test qo'shish"} < /ModalHeader>
        < AvForm
    onValidSubmit = {saveOrEditTest} >
        < ModalBody >

        < Row >
        < Col >
        < AvField
    required = {true}
    type = "text"
    label = "Title"
    className = "mt-2"
    placeholder = "title"
    defaultValue = {currentTest ? currentTest.title : ''}
    name = "title" / >
        < /Col>
        < Col >
        < AvField
    type = "select"
    defaultValue = {currentTest ? currentTest.level : ''}
    className = "mt-2"
    label = "Qiyinlik darajasi"
    placeholder = "Darajani tanlang"
    name = "level" >
        < option > Darajani
    tanlang < /option>
    < option
    value = "EASY" > Oson < /option>
        < option
    value = "MEDIUM" > O
    'rtacha</option>
    < option
    value = "HARD" > Qiyin < /option>
        < /AvField>
        < /Col>
        < Col >
        < AvField
    type = "select"
    defaultValue = {currentTest.resSubject ? currentTest.resSubject.id : ''}
    className = "mt-2"
    label = "Fan"
    placeholder = "Fanni tanlang"
    name = "subjectId" >
        < option > Fanni
    tanlang < /option>
    {
        subjectArray ? subjectArray.map(item = >
            < option value = {item.id} > {item.nameUz} < /option>
    ) :
        ''
    }
<
    /AvField>
    < /Col>
    < /Row>
    < Row >
    < Col >
    < h5 > Test
    savollarini
    kiriting:<
    /h5>
    < /Col>
    < /Row>
    {
        reqQuestionList ? reqQuestionList.map((item, ind) = >
            < Row key = {ind} >
            < Col >
            < Row >
            < Col >
            < AvField
        required = {true}
        type = "text"
        className = "mt-2"
        placeholder = "Savol"
        defaultValue = {item.question ? item.question : ''}
        key = {`question${ind}`
    }
        name = {`question/${ind}`
    }
        />
        < /Col>
        < /Row>

        < Row >

        < Col >
        < AvRadioGroup
        key = {`question${ind}/radio`
    }
        defaultValue = {item.correctAnswerr ? item.correctAnswerr : ''}
        name = {`answerRadio/${ind}`
    }
        required >
        < Row
        key = {ind} >
            {
                item.reqAnswerList ? item.reqAnswerList.map((answerItem, index) = >
                    < Col >
                    < AvRadio key = {`answerRadio${index}`
            }
        className = " m-0,p-0"
        style = {
        {
            display: "inline!important"
        }
    }
        customInput
        value = {`answerRadio/${ind}/${index}`
    }
        />
        < AvField
        style = {
        {
            display: "inline!important"
        }
    }
        className = " m-0,p-0"
        required = {true}
        type = "text"
        placeholder = "Javob variantini kiriting"
        onChange = {(e) =
    >
        getXQuestionYAnswer(e, ind, index)
    }
        defaultValue = {answerItem.answer ? answerItem.answer : ''}
        key = {`answer${index}`
    }
        name = {`answer/${index}`
    }
        />
        < /Col>
    ) :
        ''
    }


    <
        /Row>
        < /AvRadioGroup>
        < /Col>

        < /Row>

        < /Col>
        < /Row>
    ) :
        ''
    }
<
    Row >
    < Col
    md = {
    {
        size: 2, offset
    :
        5
    }
}>
<
    Button
    type = "button"
    className = "ml-5"
    color = "info"
    style = {
    {
        borderRadius: "20px"
    }
}
    onClick = {addNewRow} > + < /Button>
        < /Col>
        < /Row>
        < /ModalBody>
        < ModalFooter >
        < Button
    color = "danger"
    onClick = {addTest} > Bekor
    qilish < /Button>
    < Button
    color = "primary"
    type = "submit" > Saqlash < /Button>
        < /ModalFooter>
        < /AvForm>
        < /Modal>
        < Modal
    size = "lg"
    style = {
    {
        maxWidth: '1600px', width
    :
        '80%'
    }
}
    isOpen = {questionListModal}
    fade = {false}
    toggle = {closeQuestionListModal} >
        < ModalHeader > Savollar
    ro
    'yxati</ModalHeader>
    < ModalBody >
    < Table >
    < thead >
    < tr >
    < th >№<
    /th>
    < th > Savol < /th>
    < th > Javob
    A < /th>
    < th > Javob
    B < /th>
    < th > Javob
    C < /th>
    < th > Javob
    D < /th>
    < /tr>
    < /thead>
    < tbody >
    {
        tempQuestionList ? tempQuestionList.map((item, index) = >
            < tr >
            < td > {index +1} < /td>
            < td > {item.question} < /td>
            {
                item.resAnswers ? item.resAnswers.map((item2, index2) = >
                    < td >
                    < input type = "checkbox" checked = {item2.correct}
    />
    < span > {item2.answer} < /span>
    {
        console.log(item2, "Item2<<<<<<<<<<<<<<>..........")
    }
<
    /td>
) :
    ''
}
<
    /tr>
) :
    ''
}
<
    /tbody>
    < /Table>
    < /ModalBody>
    < ModalFooter >
    < Button
    color = "primary"
    onClick = {closeQuestionListModal}
    type = "button" > Ok < /Button>
        < /ModalFooter>
        < /Modal>
        < Modal
    isOpen = {showDeleteModal}
    toggle = {toggleCloseTestModal} >
        < ModalHeader > Rostan
    xam
    testni
    o
    'chirishni istaysizmi?</ModalHeader>

    < ModalBody >

    < /ModalBody>
    < ModalFooter >
    < Button
    color = "danger"
    onClick = {toggleCloseTestModal} > Bekor
    qilish < /Button>
    < Button
    className = "ml-3"
    color = "success"
    onClick = {testDeleteYes}
    type = "button" > O
    'chirish</Button>
    < /ModalFooter>
    < /Modal>
    < /div>
    < /div>

)
}
