import '../../App.css'
import React from 'react'
import axios from 'axios'
import { withRouter } from 'react-router-dom'

class ChangeData extends React.Component {

    constructor(props) {
        super(props)
        this.state = {
            selectedFile: {},
            userData: this.props.location.state
        }
        this.handleSubmit = this.handleSubmit.bind(this)
    }

    async handleSubmit(e) {

        e.preventDefault()

        this.setState({
            selectedFile: e.target.value
        })

        const data = {
            password: this.password,
            email: this.email,
            firstName: this.firstName,
            lastName: this.lastName,
            accountType: this.accountType,
            gender: this.gender.toUpperCase(),
            birthdayDate: this.birthdayDate
        }
        let response = axios.put('/v1/users/' + this.state.userData.id, data)

    }

    render() {
        return (
            <div>
                <h1>TRENUTNI PODACI</h1>
                <h2>Ime: {this.state.userData.name}</h2><br />
                <h2>Prezime: {this.state.userData.surname}</h2><br />
                <h2>Korisničko ime: {this.state.userData.username}</h2><br />
                <h2>E-mail adresa: {this.state.userData.email}</h2><br />
                <h1>Upišite nove podatke:</h1>
                <form onSubmit={this.handleSubmit}>
                    <label>Ime</label>
                    <input type='text' onChange={e => this.firstName = e.target.value} />
                    <br />

                    <label>Prezime</label>
                    <input type='text' onChange={e => this.lastName = e.target.value} />
                    <br />

                    <label>E-mail adresa</label>
                    <input type='email' onChange={e => this.email = e.target.value} />
                    <br />

                    <label>Lozinka</label>
                    <input type='password' onChange={e => this.password = e.target.value} />
                    <br />

                    <label>Visina u centimetrima</label>
                    <input type='text' onChange={e => this.height = e.target.value} />
                    <br />

                    <label>Težina u kilogramima</label>
                    <input type='text' onChange={e => this.weight = e.target.value} />
                    <br />

                    <h4>Spol:</h4>
                    <div>
                        <div className='choice'>
                            <input type='radio' value='MALE' name='gender' onChange={e => this.gender = e.target.value} />
                            <label>Muško</label>
                        </div>
                        <div className='choice'>
                            <input type='radio' value='FEMALE' name='gender' onChange={e => this.gender = e.target.value} />
                            <label>Žensko</label>
                        </div>
                        <div className='choice'>
                            <input type='radio' value='OTHER' name='gender' onChange={e => this.gender = e.target.value} />
                            <label>Ostalo</label>
                        </div>
                    </div>

                    <label>Datum rođenja:</label>
                    <input type='date' onChange={e => this.birthdayDate = e.target.value} />

                    <h4>Vrsta računa: </h4>
                    <div>
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
                    <button>PROMIJENI</button>
                </form>
            </div>
        )
    }
}

export default withRouter(ChangeData)