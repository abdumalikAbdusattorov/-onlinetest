import React, {useState} from 'react';
import {Button, Col, Container, Row} from "reactstrap";
import {AvField, AvForm} from "availity-reactstrap-validation";
import axios from "axios";
import { useHistory } from "react-router-dom";

export default function RegisteredPage (){
    const [checkCode,setCheckCode]=useState(false);
    const [isVerificated,setIsVerificated]=useState(false);
    const [code,setCode]=useState(false);
        const history = useHistory();
        const register=(e,v)=>{
           // console.log(v,"reg===============================");
            axios.post('http://localhost/api/auth/register',v)
                .then(res => {
                    console.log(res,"res===============================================>>>>>>>>>>>>>>")
                    localStorage.setItem('OnlineToken',res.data.body.tokenType+" "+res.data.body.accessToken);
                    axios.get('http://localhost/api/user/me',{headers:{"Authorization" : localStorage.getItem('OnlineToken')}})
                        .then(res => {
                            console.log(res,"me ==========");
                            if (res.data.object != null){
                                res.data.object.roles.map(role =>{
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

        const sendCodeToPhoneNumber=(e)=>{
           // console.log(e.target.value);
        let len=e.target.value.length;
            if (len==13){
                let sms={
                    phoneNumber:e.target.value
                };
                axios.post('http://localhost/api/sms/smsCode',sms)
                    .then(res=>{
                        console.log(res,"Code<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>")
                        setCode(res.data);
                        setCheckCode(true);

                    })

            }
        };
        const kodniTekshiradeiganMetod=(e)=>{
            let s=e.target.value.length;
            if (s==6){
                if (e.target.value==code){
                   setIsVerificated(true)
                }
            }
        };
        return (
            <div>
                <Container>
                    <Row>
                        <Col>
                            <Col md={4}
                                 className="mt-5 wrap-login100 offset-4 d-flex flex-column p-0 justify-content-center align-items-center">
                                <AvForm onValidSubmit={register}>
                                    <div className="form-group">

                                        <AvField  required={true} type="text" label="Telefon raqamingizni to'liq formatda kiriting(+998991234567)"
                                                  onChange={sendCodeToPhoneNumber} className="mt-2" placeholder="Nomeringiz kiriting"
                                                 name="phoneNumber"/>
                                        {
                                            checkCode?
                                                <AvField required={true} type="text" className="mt-2" placeholder="Kodni kiriting"
                                                        onChange={kodniTekshiradeiganMetod} name="code"/>:''
                                        }
                                        {
                                            isVerificated?
                                                <div>
                                                    <span>Email</span>
                                                    <AvField required={true} type="email" placeholder="Emailingizni kiriting"
                                                             name="email"/>
                                                    <span>Password</span>
                                                    <AvField required={true} type="password" placeholder="***"
                                                             name="password"/>
                                                    <AvField required={true} type="text"  className="mt-2" placeholder="Ism"
                                                             name="firstName"/>
                                                    <span>Last Name</span>
                                                    <AvField required={true} type="text"  placeholder="Familiya"
                                                             name="lastName"/>
                                                    <Button type="submit" className=" login100-form-btn">Ro'yxatdann o'tish</Button>
                                                </div>:''
                                        }

                                    </div>
                                </AvForm>
                            </Col>
                        </Col>
                    </Row>
                </Container>
            </div>
        );
}
