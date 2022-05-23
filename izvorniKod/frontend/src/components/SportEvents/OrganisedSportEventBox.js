import React from 'react'
import axios from 'axios'

class OrganisedSportEventBox extends React.Component {
    constructor() {
        super()
        this.state = {
            locationName: null
        }
    }
    async componentDidMount() {
        try {
            if(this.props.sevent.location) {
                const response = await axios.get("/v1/locations/" + this.props.sevent.location)
                this.setState({locationName:response.data.name})
            }
        } catch (err) {
            console.log(err)
        }
    }
    render() {
        const start_date = this.props.sevent.startDateTime.split("T")[0]
        const end_date = this.props.sevent.endDateTime.split("T")[0]
        const start_time = this.props.sevent.startDateTime.split("T")[1]
        const end_time = this.props.sevent.endDateTime.split("T")[1]

        return(
            <div className="sport-event-box"  >
                <img className="sport-icon" src={this.props.icon} alt={this.props.sevent.sport}/>
                <div>
                    <h3>{this.state.locationName}</h3>

                    <p>Sport: {this.props.sevent.sport}</p>

                    <p>Početak: {start_date + "  " + start_time}</p>

                    <p>Kraj: {end_date + "  " + end_time}</p>

                    <p>Trenutni broj sudionika: {this.props.sevent.participents.length}/{this.props.sevent.maxNumberOfParticpents}</p>

                    {this.props.sevent.cost ? <p>Cijena: {this.props.sevent.cost} kn </p> : null}
                </div>
                <div className="event-approval">
                    {this.props.sevent.eventApproved ? 
                        <p className="approved-event"> Potvrđeno! </p> : 
                        <p className="unapproved-event"> Nepotvrđeno </p>}
                </div>
            </div>
        )
    }

}

export default OrganisedSportEventBox