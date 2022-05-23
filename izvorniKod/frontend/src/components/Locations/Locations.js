import React from 'react'
import axios from 'axios'

import './Locations.css'

import LocationBox from './LocationBox'

import SportService from '../authServices/SportService'


class Locations extends React.Component {
    constructor(props) {
        super(props)
        this.state = {
            locationData: [],
            user: null,
            sports: [],
        }
        this.deleteLocation = this.deleteLocation.bind(this)
    }

    async componentDidMount() {
        try {
            const response = await axios.get(`/v1/users/${this.props.user.user_info.id}/locations`)
            let locations = response.data
            this.setState({locationData:locations, user:this.props.user})
            
        } catch (err) {
            console.log(err)
        }
        const sports = await SportService()     //dohvaćanje popisa sportova iz baze podataka
        if (sports) {
            let selectedSports ={}
            let sportIcons ={}
            for(let i = 0; i<sports.length; i++) {
                selectedSports[sports[i].name] = true
                sportIcons[sports[i].name] = sports[i].iconColorUri
            }
            this.setState({sports:sports, selectedSports:selectedSports, sportIcons:sportIcons})
        }
    }

    async deleteLocation(id) {
        try {
            if (window.confirm("Jesi li siguran/sigurna da želiš obrisati ovu lokaciju?")) {
                console.log(id)
                const response = await axios.delete(`/v1/users/${this.props.user.user_info.id}/locations/${id}`)
                console.log(response.data)
                this.setState(prevState => {
                    let newLocationData = prevState.locationData
                    const locationData = newLocationData.filter(location => location.id !== id)
                    console.log(locationData)
                    return {locationData:locationData}
                })
            }
        } catch(err) {
            console.log(err)
        }
    }

    render() {
        const locations = this.state.locationData.map(location => <LocationBox
                                                                    sports = {this.state.sports}
                                                                    key = {location.id}
                                                                    location = {location}
                                                                    deleteLocation = {this.deleteLocation}
                                                                    user= {this.state.user}/>)

        return(
            <div>
                {this.state.locationData.length >= 1 ? 
                (<h2 className="organised-title">Tvoje lokacije</h2>) :
                (<h2 className="organised-title">Nemaš kreiranih lokacija</h2>)}
                <div className="event-list">
                    {locations}
                </div>
            </div>
        )
    }
}

export default Locations