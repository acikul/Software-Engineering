import React from 'react'
import logo from '../images/FER_logo.png'
import './Footer.css'

function Footer(props) {
    return (
        <>
            <footer className={props.darkMode ? "footer-dark" : "footer-light"} >
                <div className="copyright-logo">
                    <h2>&copy;FringillaSport</h2>
                    <a href="https://www.fer.unizg.hr/"><img src={logo} alt="FER logo" /></a>
                </div>
            </footer>
        </>
    )
}

export default Footer
