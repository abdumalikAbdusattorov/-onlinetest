import React, {useState, useEffect} from 'react';
import {Link} from "react-router-dom";
import {AvFeedback, AvField, AvForm, AvGroup, AvInput, AvRadio, AvRadioGroup} from "availity-reactstrap-validation";
import {
    Button, Card, CardBody, CardImg, CardSubtitle, CardText, CardTitle,
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

export default function TestPages() {

    return (

        <div>
            <AvForm>
                <Row>
                    <AvRadioGroup inline name="asd" label="Sizning Teslaringiz" required>
                        <h4>1.Sizning eng ko'p aldagan kuningiz??</h4>
                        <AvRadio label="Bugun" value="Bugun"/>
                        <AvRadio label="Kecha" value="Kecha"/>
                        <AvRadio label="Ertaga" value="Ertaga"/>
                        <AvRadio label="Ummuman" value="Ummuman"/>
                    </AvRadioGroup>

                </Row>
                <Row>
                    <AvRadioGroup inline name="asds" required>
                        <h4>2.Amir Temur nechanchi yil tavallud topgan?</h4>
                        <AvRadio label="1336" value="1336"/>
                        <AvRadio label="1404" value="1404"/>
                        <AvRadio label="1441" value="1441"/>
                        <AvRadio label="1555" value="1555"/>
                    </AvRadioGroup>
                </Row>


                <Row>
                    <AvRadioGroup inline name="dsas" required>
                        <h4>3.Web dasturchilik nechiga bo'inadi?</h4>
                        <AvRadio label="2ga" value="2ga"/>
                        <AvRadio label="5ga" value="5ga"/>
                        <AvRadio label="6ga" value="6ga"/>
                        <AvRadio label="Bo'linmaydi" value="Bo'linmaydi"/>
                    </AvRadioGroup>

                </Row>
            </AvForm>
            <Row>
                <Button color="success">Topshirish</Button>
            </Row>
        </div>

    );

}


