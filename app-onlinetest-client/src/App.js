import React, {useState, useEffect} from 'react'
import axios from 'axios'
import {
    Badge,
    Col,
    Collapse,
    Nav,
    Navbar,
    NavbarBrand,
    NavbarText,
    NavbarToggler,
    NavItem,
    NavLink,
    Row
} from "reactstrap";

export default function App() {
    useEffect(() = > {
        if(
    !stopDidmountFunction
)
    {
        axios.get('http://localhost/api/user/me', {headers: {"Authorization": localStorage.getItem('OnlineToken')}})
            .then(res = > {
            // console.log(res,"me ==========");
            if(res.data.object != null
    )
        {
            res.data.object.roles.map(role = > {
                if(role.name === 'ROLE_ADMIN'
        )
            {
                setIsAdmin(true)
            }
            if (role.name === 'ROLE_USER') {
                setIsUser(true)
            }
        })
        }
        setStopDidmountFunction(true)
    })
    }
})
    const [stopDidmountFunction, setStopDidmountFunction] = useState(false)
    const [isAdmin, setIsAdmin] = useState(false);
    const [isUser, setIsUser] = useState(false);
    const [isOpen, setIsOpen] = useState(false);

    const toggle = () =
>
    setIsOpen(!isOpen);
    return (
        < div >
        < Row >
        < Col
    md = {4} >
        {isAdmin ?
        < div >
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
    href = "/comment" > Comment < /NavLink>
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
        < /div>
:
    isUser ?
<
    div >
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
    href = "/solveTest" > Test
    ishlash < /NavLink>
    < /NavItem>
    < NavItem >
    < NavLink
    href = "/ownHistory" > Tarix < /NavLink>
        < /NavItem>
        < NavItem >
        < NavLink
    href = "/" > Chiqish < /NavLink>
        < /NavItem>
        < /Nav>
        < /Collapse>
        < /Navbar>
        < /div>
:
    ''
}
<
    /Col>
    < Col
    md = {8} > < /Col>
        < /Row>
        < /div>
)
}