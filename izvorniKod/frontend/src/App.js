import Navbar from './components/Navbar/Navbar'
import React from 'react'
import Home from './components/pages/Home'
import Login from './components/pages/Login'
import Register from './components/pages/Register'
import User from './components/pages/User'
import Footer from './components/Footer/Footer'
import MapView from './components/MapView/MapView'
import ChangeData from './components/pages/ChangeData'
import LocationForm from './components/LocationForm/LocationForm'
import { BrowserRouter as Router, Switch, Route } from 'react-router-dom'
import './App.css';
import Calendar from './components/pages/Calendar'
import RCalendar from './components/pages/RCalendar'
import DataService from './components/authServices/DataService'
import SportEvents from './components/SportEvents/SportEvents'
import CoachDocumentations from './components/pages/CoachDocumentations'
import UsersList from './components/pages/UsersList'
import SportEventForm from './components/SportEventForm/SportEventForm'
import Locations from './components/Locations/Locations'

class App extends React.Component {

    constructor() {
        super()
        this.state = {
            user: null
        }
        this.changeUser = this.changeUser.bind(this)
    }

    async componentDidMount() {
        let userData = await DataService()
        console.log(userData)
        if (userData) {
            this.setState({
                user: userData
            })
        }
    }

    changeUser = (user) => {
        this.setState({
            user: user
        })
    }

    render() {
        return (
            <div className={this.state.darkMode ? "dark" : "light"}>
                <Router>
                    <Navbar changeUser={this.changeUser} user={this.state.user} />
                    <Switch>
                        <Route exact path='/' render={props => <Home user={this.state.user} />} />
                        <Route exact path='/prijava' render={props => <Login user={this.state.user} changeUser={this.changeUser} />} />
                        <Route exact path='/registracija' render={props => <Register user={this.state.user} changeUser={this.changeUser} />} />
                        <Route exact path='/podaci' render={props => <User user={this.state.user} />} />
                        <Route exact path='/karta' render={props => <MapView user={this.state.user} />} />
                        <Route exact path='/kalendar' render={props => <Calendar user={this.state.user} />} />
                        <Route exact path='/Rkalendar' render={props => <RCalendar user={this.state.user} />} />
                        <Route exact path='/changeData' render={props => <ChangeData user={this.state.user} />} />
                        <Route exact path='/stvorilokaciju' render={props => <LocationForm user={this.state.user} />} />
                        <Route exact path='/mojaokupljanja' render={props => <SportEvents user={this.state.user} />} />
                        <Route exact path='/coachDocumentations' render={props => <CoachDocumentations />} />
                        <Route exact path='/usersList' render={props => <UsersList />} />
                        <Route exact path='/stvoriokupljanje' render={props => <SportEventForm user={this.state.user} />} />
                        <Route exact path='/mojelokacije' render={props => <Locations user={this.state.user} />} />
                    </Switch>
                    <Footer darkMode={this.state.darkMode} />
                </Router>
            </div >
        )
    }
}

export default App;
