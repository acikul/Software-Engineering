import React from 'react'
import { Link } from 'react-router-dom'
import axios from 'axios'

class SelectedSportEventBox extends React.Component {
    constructor() {
        super()
        this.state = {
            locationName: null,
            userEventStatus: false,
            numberofParticpents: 0,
        }
        this.registerToEvent = this.registerToEvent.bind(this)
        this.removeFromEvent = this.removeFromEvent.bind(this)
    }

    async componentDidMount() {
        try {
            this.setState({numberofParticpents:this.props.sevent.participents.length})
            const location = await axios.get(`/v1/locations/${this.props.sevent.location}`)
            this.setState({locationName:location.data.name})
            if (this.props.user) {
                const userEventStatus = await axios.get(`/v1/users/${this.props.user.user_info.id}
                                                        /sportevents/participate/${this.props.sevent.id}`)
                this.setState({userEventStatus:true})
            }
        } catch(err) {
            console.log(err)
        }
    }

    async registerToEvent() {
        try {
            if(window.confirm("Jesi li siguran/sigurna da se želiš prijaviti na ovo sportsko okupljanje?")) {
                const userEventStatus = await axios.post(`/v1/users/${this.props.user.user_info.id}
                                                            /sportevents/participate/${this.props.sevent.id}`)
                if(userEventStatus) {
                    this.setState(prevState => {
                        const numberofParticpents = prevState.numberofParticpents + 1
                        return {userEventStatus:true,numberofParticpents:numberofParticpents}
                    })
                    alert("Uspješno si se prijavio/prijavila")
                } else {
                    alert("Došlo je do pogreške")
                }
            }
        } catch(err) {
            alert("Došlo je do pogreške")
            console.log(err)
        }
    }

    async removeFromEvent() {
        try {
            if (window.confirm("Jesi li siguran/sigurna da se želiš odjaviti s ovog sportskog okupljanja?")) {
                
                const userEventStatus = await axios.delete(`/v1/users/${this.props.user.user_info.id}
                                                            /sportevents/participate/${this.props.sevent.id}`)
                if(userEventStatus) {
                    this.setState(prevState => {
                        const numberofParticpents = prevState.numberofParticpents - 1
                        return {userEventStatus:false,numberofParticpents:numberofParticpents}
                    })
                    alert("Uspješno si se odjavio/odjavila")
                } else {
                    alert("Došlo je do pogreške")
                }
            }
        } catch(err) {
            alert("Došlo je do pogreške")
            console.log(err)
        }
    }

    render() {
        const start_date = this.props.sevent.startDateTime.split("T")[0]
        const end_date = this.props.sevent.endDateTime.split("T")[0]
        const start_time = this.props.sevent.startDateTime.split("T")[1]
        const end_time = this.props.sevent.endDateTime.split("T")[1]
        console.log(this.props.sevent)

        let message 
        if (this.props.user) {
            if (this.state.userEventStatus === true) {
                message =  (<div> 
                                <p>Na ovo sportsko okupljanje si prijavljen(a).</p>
                                <p>Želiš li se odjaviti?</p>
                                <h3 className="register-button" onClick={() => this.removeFromEvent()}>Odjava</h3>
                            </div>)
            } else {
                console.log(this.props.sevent.participents.length, this.props.sevent.maxNumberOfParticpents)
                if (this.props.sevent.participents.length >= this.props.sevent.maxNumberOfParticpents) {
                    message = (<p>Maksimalan broj sudionika je već prijavljen na ovo sportsko okupljanje</p>)
                } else {
                    message = (<p className="register-button" onClick={() => this.registerToEvent()}>Prijavi se na ovo sportsko okupljanje</p>)
                }
            }
        } else {
            message =  (<div>
                            <p>Moraš biti prijavljen(a) u sustav kako bi se mogao/mogla prijaviti na sportsko okupljanje</p>
                            <Link to="/prijava">
                                <h3 className="register-button">Prijavi se</h3>
                            </Link>
                        </div>)
        }


        return(
            <div className="sport-event-box selected-sport-event-box">
                <div>
                    <img className="selected-sport-icon" src={this.props.icon} alt={this.props.sevent.sport}/>
                </div>
                <div>
                    <h3>{this.state.locationName}</h3>

                    <p>Sport: {this.props.sevent.sport}</p>

                    <p>Početak: {start_date + "  " + start_time}</p>

                    <p>Kraj: {end_date + "  " + end_time}</p>

                    <p>Organizator: {this.props.sevent.organizer}</p>

                    {/*<p>Vrsta sportskog događaja: {type_event} događaj</p>*/}

                    <p>Trenutni broj sudionika: {this.state.numberofParticpents}/{this.props.sevent.maxNumberOfParticpents}</p>

                    {this.props.sevent.cost ? <p>Cijena: {this.props.sevent.cost} kn </p> : null}
                </div>
                <div className="event-register">
                    {message}
                </div>
            </div>
        )
    }

}

export default SelectedSportEventBox