import React from 'react'
import axios from 'axios'

import './SportEvents.css'

import SportService from '../authServices/SportService'
import OrganisedSportEventBox from './OrganisedSportEventBox'

class SportEvents extends React.Component {
    constructor() {
        super()
        this.state = {
            eventsData: [],
            sports: [],
            sportIcons: {},
            user: null,
        }
    }


    async componentDidMount() {
        const sports = await SportService()     //dohvaćanje popisa sportova iz baze podataka
        if (sports) {
            let sportIcons ={}
            for(let i = 0; i<sports.length; i++) {
                sportIcons[sports[i].name] = sports[i].iconColorUri
            }
            
            this.setState({sports:sports, sportIcons:sportIcons})
        }
        try {
            console.log(this.props.user.user_info)
            const response = await axios.get(`/v1/users/${this.props.user.user_info.id}/sportevents/organize`)
            let events = response.data
            for (let i = 0; i<events.length; i++) {
                let sportName = await axios.get("/v1/sports/" + events[i].sport)
                if (sportName) {
                    console.log(sportName.data.name)
                    events[i].sport = sportName.data.name
                }
            }
            console.log(events)
            this.setState({eventsData:events, user:this.props.user})
            
        } catch (err) {
            console.log(err)
        }
        
    }


    render() {
        const events = this.state.eventsData.map(sevent => <OrganisedSportEventBox 
                                                                icon={this.state.sportIcons[sevent.sport]}
                                                                key = {sevent.id} 
                                                                sevent = {sevent}/>)
        
        return(
            <div>
                {events.length >= 1 ? 
                (<h2 className="organised-title">Sportska okupljanja koja si ti organizirao!</h2>) :
                (<h2 className="organised-title">Nema organiziranih sportskih okupljanja</h2>)}
                <div className="event-list">
                    {events}
                </div>
            </div>
        )
    }
}

export default SportEvents