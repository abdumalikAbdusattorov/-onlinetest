import React, {Component, useState} from 'react';
import PropTypes from 'prop-types';
import {
    Button, Col,
    Collapse, Input, Label,
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


export default function UserCabinet() {
    const toggle1 = () =
>
    setIsOpen(!isOpen);
    const [isOpen, setIsOpen] = useState(false);
    return (
        < div >
        < h3 > User
    Cabinet < /h3>
    < Row >
    < Col >
    < Navbar
    color = "light"
    light
    expand = "md" >
        < NavbarToggler
    onClick = {toggle1}
    />
    < Collapse
    navbar >
    < Nav
    className = "mr-auto"
    navbar >
    < NavItem >
    < NavLink
    href = "/solveTest" > Test
    ishlash < /NavLink>
    < /NavItem>
    < NavItem >
    < NavLink
    href = "/ownHistory" > Tarix < /NavLink>
        < /NavItem>
        < NavItem >
        < NavLink
    href = "/comment" > Commentlar < /NavLink>
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
        < /div>

)
    ;
}
