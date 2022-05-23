import '../../App.css'
import React from 'react'
import LoginService from '../authServices/LoginService'
import { Link, Redirect } from 'react-router-dom'
import './LoginSection.css'
import login from '../images/login.svg'
import avatar from '../images/avatar.svg'
import UserTesting from './UserTesting'

class Login extends React.Component {

    constructor() {
        super()
        this.handleSubmit = this.handleSubmit.bind(this)
    }

    handleSubmit = e => {
        e.preventDefault()
        const data = {
            username: this.username,
            password: this.password
        }
        LoginService(data, this.props.changeUser)
    }

    render() {

        if (this.props.user) {
            return <Redirect to="/podaci" />
        }


        return (
            <>
                <div className="container">

                    <div className="login-content">
                        <form onSubmit={this.handleSubmit}>

                            <img src={avatar} alt="Avatar" />
                            <h2 className="title">Prijava</h2>

                            <div className="input-div one">

                                <div className="i">
                                    <i className="fas fa-user" aria-hidden="true" />
                                </div>

                                <div className="div">
                                    <input placeholder="Korisničko ime" type='username' onChange={e => this.username = e.target.value} />
                                </div>

                            </div>

                            <div className="input-div pass">

                                <div className="i">
                                    <i className="fas fa-lock" aria-hidden='true' />
                                </div>

                                <div className="div">
                                    <input placeholder="Lozinka" type="password" onChange={e => this.password = e.target.value} />
                                </div>

                            </div>

                            <button className="btn-login">Prijava</button>

                            <Link to='/registracija'>
                                Novi korisnik?
                        </Link>

                        </form>

                    </div>

                    <div className="img">
                        <img src={login} alt="" />
                    </div>

                </div>
                <h3 className="tempK">Korisnici (za testiranje)</h3>
                <UserTesting />
            </>
        )
    }
}

export default Login;
