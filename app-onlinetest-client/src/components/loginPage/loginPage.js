import React, {Component} from 'react'
import {Button, Col, Container, Row} from "reactstrap";
import {AvField, AvForm} from "availity-reactstrap-validation";
import axios from 'axios'
import {Link} from "react-router-dom";
import {useHistory} from "react-router-dom";


export default function LoginPage() {

    const history = useHistory();
    const login = (e, v) => {
        //console.log(v, "valu==-");
        axios.post('http://localhost/api/auth/login', v)
            .then(res => {
                localStorage.setItem('OnlineToken', res.data.body.tokenType + " " + res.data.body.accessToken);
                axios.get('http://localhost/api/user/me', {headers: {"Authorization": localStorage.getItem('OnlineToken')}})
                    .then(res => {
                        // console.log(res,"me ==========");
                        if (res.data.object != null) {
                            res.data.object.roles.map(role => {
                                if (role.name === 'ROLE_ADMIN') {
                                    history.push("/adminCabinet")
                                }
                                if (role.name === 'ROLE_USER') {
                                    history.push("/userCabinet")
                                }
                            })
                        }
                    })
            });

    };
    return (
        <div>
            <div>
                <Container>
                    <Row className="mt-5">
                        <Col md={{size: 6, offset: 3}}
                             className="mt-5">
                            <AvForm onValidSubmit={login}>
                                <Row>
                                    <Col>
                                        <AvField required={true} type="text"
                                                 label="Telefon raqamizni yoki emailizni kiriting" className="mt-2"
                                                 placeholder="Login"
                                                 name="phoneNumberOrEmail"/>
                                        <AvField required={true} type="password" label="Parolizni kiriting"
                                                 placeholder="***..." name="password"/>
                                    </Col>
                                </Row>
                                <Row>
                                    <Col></Col>
                                    <Col>
                                        <Link to="/register">
                                            <span>Ro'xhatdan o'tish</span>
                                        </Link>
                                    </Col>
                                    <Col></Col>
                                    <Col className="ml-5">
                                        <Button type="submit" color="success">Kirish</Button>
                                    </Col>
                                </Row>


                            </AvForm>
                        </Col>
                    </Row>
                </Container>
            </div>
        </div>
    );
}
