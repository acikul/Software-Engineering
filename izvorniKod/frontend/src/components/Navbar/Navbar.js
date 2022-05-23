import React, { useState } from 'react'
import { Link } from 'react-router-dom'
import './Navbar.css'
import LogoutService from '../authServices/LogoutService'
import { useLocation } from 'react-router-dom'



function Navbar(props) {
    const [click, setClick] = useState(false);

    let logButton, userPage, calendar, sportEventOrganizer, sportEventMap

    let NavbarLinks = []
    
    const location = useLocation().pathname;

    NavbarLinks.push((
        <li>
            <Link to='/' className='naslovnica' onClick={() => setClick(false)}>
                Naslovnica
            </Link>
        </li>
    ))

    if(!props.user){
        NavbarLinks.push((
            <li>
                <Link to='/ofringili' className='ofringili' onClick={() => setClick(false)}>
                    O Fringilli
                </Link>
            </li>
        ))
    }

    if(!props.user){
        NavbarLinks.push((
            <li>
                <Link to='/kontakt' className='kontakt' onClick={() => setClick(false)}>
                    Kontakt
                </Link>
            </li>
        ))
    }

    if(!(location == "/karta") && (!props.user || ( props.user && !props.user.roles.includes("ROLE_RENTER")))){
        NavbarLinks.push((
            <li>
                <Link to='/karta' className='kartaSportskihOkupljanja' onClick={() => setClick(false)}>
                    Karta sportskih okupljanja 
                </Link>
            </li>
        ))
    }

    if(!(location == "/Rkalendar") && props.user && props.user.roles.includes("ROLE_RENTER")){
        NavbarLinks.push((
            <li>
                <Link to='/Rkalendar' className='renterKalendar' onClick={() => setClick(false)}>
                    Kalendar rezervacija
                </Link>
            </li>
        ))
    }

    if(props.user){
        NavbarLinks.push((
            <li>
                <Link to='/stvorilokaciju' className='stvoriLokaciju' onClick={() => setClick(false)}>
                    Stvori lokaciju
                </Link>
            </li>
        ))
    }

    if(!(location == "/kalendar") && props.user && props.user.roles.includes("ROLE_ATHLETE")){
        NavbarLinks.push((
            <li>
                <Link to='/kalendar' className='renterKalendar' onClick={() => setClick(false)}>
                    Kalendar sportskih okupljanja
                </Link>
            </li>
        ))
    }

    if(!(location == "/podaci") && props.user){
        NavbarLinks.push((
            <li>
                <Link to='/podaci' className='renterKalendar' onClick={() => setClick(false)}>
                    Moji podaci
                </Link>
            </li>
        ))
    }

    if(props.user){
        NavbarLinks.push((
            <li>
                <Link to='/' className='kontakt' onClick={() => LogoutService(props.changeUser)}>
                        Odjava
                </Link>
            </li>
        ))
    }

    if(!(location == "/prijava") && !props.user){
        NavbarLinks.push((
            <li>
                <Link to='/prijava' className='kontakt' onClick={() => setClick(false)}>
                    Prijava
                </Link>
            </li>
        ))
    }

    return (
        <>
            <div className={props.darkMode ? "nav-container-black" : "nav-container"}>
                <nav>
                    <Link to="/" className="logo">
                        FringillaSport
                    </Link>
                    <div className={props.darkMode ? "menu-icon-dark" : "menu-icon"} onClick={() => setClick(!click)}>
                        <i className={click ? 'fas fa-times' : 'fas fa-bars'} />
                    </div>
                    <ul className={(click && props.darkMode) ? 'nav-menu-dark active' : (click && !props.darkMode) ? 'nav-menu active' : 'nav-menu'} >
                        

                    {NavbarLinks.map(function(name, index){
                        return name;
                    })}
                        
                    </ul>
                </nav>
            </div>
        </>
    )
}

export default Navbar
