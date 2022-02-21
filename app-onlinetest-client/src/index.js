import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import {Container} from "react-bootstrap";
import {BrowserRouter as Router, Route, Switch} from "react-router-dom";
import RegisteredPage from "./components/loginPage/registeredPage";
import LoginPage from "./components/loginPage/loginPage";
import AdminCabinet from "./AdminCabinet";
import UserCabinet from "./UserCabinet";
import Test from "./components/pagesAdmin/test";
import Block from "./components/pagesAdmin/block";
import TestBlock from "./components/pagesAdmin/testBlock";
import HistoryUser from "./components/pagesAdmin/historyUser";
import Comment from "./components/pagesAdmin/comment";
import Subject from "./components/pagesAdmin/subject";
import SolveTest from "./components/pagesUser/solveTest";
import OwnHistory from "./components/pagesUser/ownHistory";
import TestPages from "./components/pagesUser/testPages";

ReactDOM.render(

< React.StrictMode >
< Container >
< Router >
< Switch >
< Route
path = "/subject" >
    < Subject / >
    < /Route>
    < Route
path = "/comment" >
    < Comment / >
    < /Route>
    < Route
path = "/historyUser" >
    < HistoryUser / >
    < /Route>
    < Route
path = "/testBlock" >
    < TestBlock / >
    < /Route>
    < Route
path = "/block" >
    < Block / >
    < /Route>
    < Route
path = "/test" >
    < Test / >
    < /Route>
    < Route
path = "/adminCabinet" >
    < AdminCabinet / >
    < /Route>
    < Route
path = "/userCabinet" >
    < UserCabinet / >
    < /Route>
    < Route
path = "/register" >
    < RegisteredPage / >
    < /Route>
    < Route
path = "/solveTest" >
    < SolveTest / >
    < /Route>
    < Route
path = "/ownHistory" >
    < HistoryUser / >
    < /Route>
    < Route
path = "/" >
    < LoginPage / >
    < /Route>
    < /Switch>
    < /Router>
    < /Container>
    < /React.StrictMode>,
document.getElementById('root')
)
;

