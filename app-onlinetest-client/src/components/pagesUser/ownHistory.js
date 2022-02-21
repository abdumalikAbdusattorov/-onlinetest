import React, {Component, useState} from 'react';
import {Link} from "react-router-dom";
import {
    Button,
    Col,
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

export default function OwnHistory (){
    const toggle1 = () => setIsOpen(!isOpen);
    const [isOpen, setIsOpen] = useState(false);
        return (
            <div>
                <Row>
                    <Col>
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
                                        <NavLink href="/">Chiqish</NavLink>
                                    </NavItem>

                                </Nav>
                            </Collapse>
                        </Navbar>
                    </Col>
                </Row>
                <h3>Tarix</h3>
            </div>
        );

}
