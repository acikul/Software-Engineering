import '../../App.css'
import React from 'react'
import RegisterService from '../authServices/RegisterService'
import register from '../images/register.svg'
import avatar from '../images/avatar.svg'
import { Redirect } from 'react-router-dom'
import './RegisterSection.css'

class Register extends React.Component {

    constructor() {
        super()
        this.state = {}
        this.handleSubmit = this.handleSubmit.bind(this)
    }

    setAccount(e) {
        this.accountType = e.target.value
    }

    handleSubmit = e => {

        e.preventDefault()

        const data = {
            firstName: this.firstName,
            lastName: this.lastName,
            username: this.username,
            email: this.email,
            accountType: this.accountType,
            password: this.password
        }

        RegisterService(data, this.props.changeUser)

    }

    render() {
        if (this.props.user) {
            return <Redirect to="/podaci" />
        }

        return (

            <div className='container'>

                <div className="img">
                    <img src={register} alt="" />
                </div>
                <div className="register-content">
                    <form onSubmit={this.handleSubmit}>


                        <h2 className="title">Registracija</h2>

                        <div className="input-div one">
                            <div className="i">
                                <i className="fas fa-user" aria-hidden="true" />
                            </div>

                            <div className="div">
                                <input placeholder="Ime" type='text' onChange={e => this.firstName = e.target.value} />
                            </div>

                        </div>

                        <div className="input-div one">
                            <div className="i">
                                <i className="fas fa-user" aria-hidden="true" />
                            </div>

                            <div className="div">
                                <input placeholder="Prezime" type='text' onChange={e => this.lastName = e.target.value} />
                            </div>

                        </div>

                        <div className="input-div one">
                            <div className="i">
                                <i className="fas fa-user-tag" aria-hidden="true" />
                            </div>

                            <div className="div">
                                <input placeholder="Korisničko ime" type='text' onChange={e => this.username = e.target.value} />
                            </div>

                        </div>

                        <div className="input-div one">
                            <div className="i">
                                <i className="fas fa-envelope" aria-hidden="true" />
                            </div>

                            <div className="div">
                                <input placeholder="Email" type='email' onChange={e => this.email = e.target.value} />
                            </div>

                        </div>

                        <div className="input-div pass">
                            <div className="i">
                                <i className="fas fa-lock" aria-hidden="true" />
                            </div>

                            <div className="div">
                                <input placeholder="Lozinka" type='password' onChange={e => this.password = e.target.value} />
                            </div>

                        </div>

                        <h4>Vrsta računa: </h4>
                        <div onChange={e => this.setAccount(e)} >
                            <div className="choice">
                                <input type='radio' value='athlete' name='account' onChange={e => this.accountType = e.target.value} />
                                <label>Sportaš</label>
                            </div>
                            <div className='choice'>
                                <input type='radio' value='coach' name='account' onChange={e => this.accountType = e.target.value} />
                                <label>Trener</label>
                            </div>
                            <div className="choice">
                                <input type='radio' value='renter' name='account' onChange={e => this.accountType = e.target.value} />
                                <label>Iznajmljivač</label>
                            </div>
                        </div>
                        <button className="btn-register">Registracija</button>
                    </form>
                </div>


            </div>
        )
    }
}

export default Register;
