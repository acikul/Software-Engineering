import '../../App.css'
import React from 'react'
import { Link } from 'react-router-dom'
import axios from 'axios'
import SportEvents from '../SportEvents/SportEvents'
import UsersList from './UsersList'
import CoachDocumentations from './CoachDocumentations'
import Locations from '../Locations/Locations'
class User extends React.Component {

    constructor() {
        super()
        this.state = {
            changeButton: null,
            usersLink: null,
            documentationsLink: null,
            documentations: null,
            uploadDocumentation: null,
            selectedSports: [],
            selectedFile: null,
            eventList: null,
            locationList: null
            
        }
        this.handleSubmit = this.handleSubmit.bind(this)
    }

    onFileChange(e) {
        e.preventDefault()
        this.setState({
            selectedFile: e.target.files[0]
        })
    }

    async handleSubmit(e) {
        e.preventDefault()
        const formData = new FormData()
        formData.append('file', this.state.selectedFile)
        const blob = new Blob([JSON.stringify({ "sportsIds": this.state.selectedSports.map(function (item) { return parseInt(item, 10); }) })], {
            type: 'application/json'
        });
        formData.append("documentation_info", blob);
        let response = await axios.post('/v1/users/' + this.props.user.user_info.id + '/documentations', formData, {
            headers: {
                'Content-type': 'multipart/form-data'
            }
        })
        alert('Dokumentacija predana')
    }

    async componentDidMount() {

        this.setState({
            changeButton:
                <Link to={{
                    pathname: '/changeData',
                    state: {
                        name: this.props.user.user_info.name,
                        surname: this.props.user.user_info.surname,
                        email: this.props.user.user_info.email,
                        username: this.props.user.user_info.username,
                        id: this.props.user.user_info.id
                    }
                }}>
                    Promijeni osobne podatke
                </Link>
        })
        if (this.props.user.roles.includes('ROLE_COACH') || this.props.user.roles.includes('ROLE_ATHLETE')) {
            this.setState({
                eventList:
                    <Link to='/mojaokupljanja' onClick={() => <SportEvents user={this.state.user} />}>
                        Popis sportskih okupljanja koja si ti organizirao
                    </Link>
            })
        }
        if (this.props.user.roles.includes('ROLE_COACH') || this.props.user.roles.includes('ROLE_ATHLETE') ||
            this.props.user.roles.includes('ROLE_RENTER')) {
            this.setState({
                locationList:
                    <Link to='/mojelokacije' onClick={() => <Locations user={this.state.user} />}>
                        Popis lokacija koje si kreirao
                    </Link>
            })
        }
        if (this.props.user.roles.includes('ROLE_ADMINISTRATOR')) {
            this.setState({
                usersLink:
                    <Link to='/usersList' onClick={() => <UsersList user={this.state.user} />}>
                        Otvori popis svih korisnika
                    </Link>
            })

            this.setState({
                documentationsLink:
                    <Link to='/coachDocumentations' onClick={() => <CoachDocumentations user={this.state.user} />}>
                        Otvori popis neobrađenih certifikata
                    </Link>
            })
        }

        if (this.props.user.roles.includes('ROLE_COACH')) {
            let sports = (await axios.get('/v1/sports')).data
            this.setState({
                uploadDocumentation:
                    <form onSubmit={this.handleSubmit}>
                        <h2>Odaberite sportove i certifikat:</h2>
                        {sports.map(element => (
                            <div>
                                <input type='checkbox' value={element.id} name='sport' onChange={e => this.setState({ selectedSports: [...this.state.selectedSports, e.target.value] })} />
                                <label>{element.name.toUpperCase()}</label>
                            </div>
                        ))}
                        <input type='file' onChange={e => this.setState({ selectedFile: e.target.files[0] })} />
                        <button>Upload</button>
                    </form>
            })
        }
    }

    render() {
        if (this.props.user.roles.includes('ROLE_ADMINISTRATOR')) {
            return (
                <div>
                    <h2>Korisničko ime: {this.props.user.user_info.username}</h2>
                    {this.state.usersLink}
                    <br />
                    {this.state.documentationsLink}
                </div>
            )
        }
        else if (this.props.user.roles.includes('ROLE_COACH')) {
            let spol = ''
            switch (this.props.user.user_info.gender) {
                case 'MALE':
                    spol = 'muški'
                    break
                case 'FEMALE':
                    spol = 'ženski'
                    break
                case 'OTHER':
                    spol = 'ostalo'
                    break
                default:
                    spol = 'nepoznato'
                    break
            }
            return (
                <div>
                    <h2>Ime: {this.props.user.user_info.name}</h2><br />
                    <h2>Prezime: {this.props.user.user_info.surname}</h2><br />
                    <h2>Korisničko ime: {this.props.user.user_info.username}</h2><br />
                    <h2>E-mail adresa: {this.props.user.user_info.email}</h2><br />
                    <h2>Spol: {this.spol}</h2>
                    <h2>Datum rođenja: {this.props.user.user_info.birthdayDate}</h2>
                    {this.state.changeButton}
                    {this.state.uploadDocumentation}
                    <br/>
                    {this.state.eventList}
                    <br/>
                    {this.state.locationList}
                </div>
            )
        }
        else if (this.props.user.roles.includes('ROLE_RENTER')) {
            return (
                <div>
                    <h2>Ime: {this.props.user.user_info.name}</h2><br />
                    <h2>Prezime: {this.props.user.user_info.surname}</h2><br />
                    <h2>Korisničko ime: {this.props.user.user_info.username}</h2><br />
                    <h2>E-mail adresa: {this.props.user.user_info.email}</h2><br />
                    {this.state.changeButton}
                    <br/>
                    {this.state.locationList}
                </div>
            )
        }
        else if (this.props.user.roles.includes('ROLE_ATHLETE')) {
            let spol = ''
            switch (this.props.user.user_info.gender) {
                case 'MALE':
                    spol = 'muški'
                    console.log(3)
                    break
                case 'FEMALE':
                    spol = 'ženski'
                    console.log(4)
                    break
                case 'OTHER':
                    spol = 'ostalo'
                    console.log(5)
                    break
                default:
                    spol = ''
                    break
            }
            return (
                <div>
                    <h2>Ime: {this.props.user.user_info.name}</h2><br />
                    <h2>Prezime: {this.props.user.user_info.surname}</h2><br />
                    <h2>Korisničko ime: {this.props.user.user_info.username}</h2><br />
                    <h2>E-mail adresa: {this.props.user.user_info.email}</h2><br />
                    <h2>Spol: {this.spol}</h2>
                    <h2>Datum rođenja: {this.props.user.user_info.birthdayDate}</h2>
                    {this.state.changeButton}
                    <br/>
                    {this.state.eventList}
                    <br/>
                    {this.state.locationList}
                </div>
            )
        }
    }
}
export default User
