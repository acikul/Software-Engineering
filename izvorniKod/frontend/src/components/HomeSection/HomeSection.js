import React from 'react'
import './HomeSection.css'
import logo from '../images/goal.svg'
import logoDark from '../images/goalDark.svg'
import karta from '../images/karta.svg'
import kartaDark from '../images/kartaDark.svg'
import sports from '../images/sports.svg'
import sportsDark from '../images/sportsDark.svg'
import casual from '../images/casual.svg'
import casualDark from '../images/casualDark.svg'
import { Link } from 'react-router-dom'

function HomeSection(props) {
    return (
        <>
            <section className="home">
                <div className="showcase">
                    <div className={props.darkMode ? "text-div-dark" : "text-div"}>
                        <h2 className="title">Sportska Revolucija</h2>
                        <h2 className="subtext">Igrajte bilo koji sport, bilo gdje</h2>
                        <h2 className="subtext2">i bilo kada.</h2>
                        <div className="button-style">
                            <Link to="/registracija" className={props.darkMode ? "button-dark" : "button"}>
                                POČETAK
                            </Link>
                        </div>
                    </div>
                    <img src={props.darkMode ? logoDark : logo} alt="goal" className="goalImg" />
                </div>
            </section>
            <section className="advertising">
                <div className={props.darkMode ? "card-dark" : "card"}>
                    <div className="face face1">
                        <div className="content">
                            <img src={props.darkMode ? sportsDark : sports} alt="" />
                        </div>
                    </div>
                    <div className="face face2">
                        <div className="content">
                            <p>Više od 10 <br />
                            različitih sportova.
                                </p>
                        </div>
                    </div>
                </div>
                <div className={props.darkMode ? "card-dark" : "card"}>
                    <div className="face face1">
                        <div className="content">
                            <img src={props.darkMode ? kartaDark : karta} alt="" />
                        </div>
                    </div>
                    <div className="face face2">
                        <div className="content">
                            <p>Preko 100 lokacija <br />
                            diljem Hrvatske.
                            </p>
                        </div>
                    </div>
                </div>
                <div className={props.darkMode ? "card-dark" : "card"}>
                    <div className="face face1">
                        <div className="content">
                            <img src={props.darkMode ? casualDark : casual} alt="" />
                        </div>
                    </div>
                    <div className="face face2">
                        <div className="content">
                            <p>Prilagođeno za sportaše<br />
                            i rekreativce.
                            </p>
                        </div>
                    </div>
                </div>
            </section>
        </>
    )
}

export default HomeSection
