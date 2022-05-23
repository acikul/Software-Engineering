import axios from 'axios'
import React from 'react'

import {Map, TileLayer } from 'react-leaflet'

import EventMarker from '../MapView/EventMarker'
import EventPolygon from '../MapView/EventPolygon'
import SelectedLocationBox from './SelectedLocationBox'

import SportService from '../authServices/SportService'

import './SportEventForm.css'

class SportEventForm extends React.Component {
    constructor() {
        super()
        this.state = {
            position:"",
            locations: [],
            selectedLocation: null,
            sports: [],
            sport: 1,
            startDate: "2021-01-01", //defualt vrijednosti datuma i vremena
            startTime: "00:00",
            endDate: "2021-01-01",
            endTime: "00:00",
            maxNumberOfParticpents: 0,
            sportEventType: "free",
            cost: 0
        }
        this.handleChange = this.handleChange.bind(this)
        this.changeSport = this.changeSport.bind(this)
        this.selectLocation = this.selectLocation.bind(this)
    }

    async componentDidMount() {
        const sports = await SportService()
        this.setState({sports:sports})  
    }

    selectLocation = (location) => {
        this.setState({selectedLocation:location})
    }

    changeSport(event) {
        const { value } = event.target
        let filteredLocations = []
        for(let j = 0; j<this.state.locations.length; j++) {
            for(let i = 0; i<this.state.locations[j].holdsSportEvent.length; i++) {
                if (value == this.state.locations[j].holdsSportEvent[i]) {
                    filteredLocations.push(this.state.locations[j])
                }
            }
        }
        this.setState({sport:value, filteredLocations:filteredLocations})
    }

    handleChange(event) {
        const {value, name} = event.target
        this.setState({[name]:value})
    }

    async searchLocations() {
        const sportEvent = {
            maxNumberOfParticpents: this.state.maxNumberOfParticpents,
            sportEventType: this.state.sportEventType,
            sportId: this.state.sport,
        }
        sportEvent.startDateTime = this.state.startDate + "T" + this.state.startTime + ":00"
        sportEvent.endDateTime = this.state.endDate + "T" + this.state.endTime + ":00"
        if (this.state.sportEventType === "paid") {
            sportEvent.cost = this.state.cost
        } else {
            sportEvent.cost = null
        }
        console.log(sportEvent)
        try {
            let suggestedLocations = await axios.post(`/v1/users/${this.props.user.user_info.id}/sportevents/organize/0/location_inquery/presuggest`, sportEvent)
            console.log(suggestedLocations.data)
            if (suggestedLocations.data.length >= 1) {
                for(let j = 0; j<suggestedLocations.data.length; j++) {
                    const newGPXCoordinates = suggestedLocations.data[j].gpxCoordinates.split("").filter(c => {return c!=="[" && c!=="]"}).join("").split(",")
                    suggestedLocations.data[j].gpxCoordinates = []
                    for (let i = 0; i<newGPXCoordinates.length; i=i+2) {
                        suggestedLocations.data[j].gpxCoordinates.push([newGPXCoordinates[i],newGPXCoordinates[i+1]])
                    }
                    
                }
            } else {
                alert("Nažalost, za odabrane parametere nema prikladnih lokacija")
            }
            this.setState({locations:suggestedLocations.data})
            
        } catch(err) {
            console.log(err)
        }

    }

    async submitSportEvent() {
        if (this.state.selectedLocation) {
            const sportEvent = {
                location: this.state.selectedLocation.id,
                maxNumberOfParticpents: this.state.maxNumberOfParticpents,
                sportEventType: this.state.sportEventType,
                sportId: this.state.sport,
            }
            sportEvent.startDateTime = this.state.startDate + "T" + this.state.startTime + ":00"
            sportEvent.endDateTime = this.state.endDate + "T" + this.state.endTime + ":00"
            if (this.state.sportEventType === "paid") {
                sportEvent.cost = this.state.cost
            }
            console.log(sportEvent)
            try {
                let organizeResponse = await axios.post(`/v1/users/${this.props.user.user_info.id}/sportevents/organize`,sportEvent)
                console.log(organizeResponse.data)
                let locationResponse = await axios.post(`/v1/users/${this.props.user.user_info.id}/sportevents/organize/${organizeResponse.data.id}/location_inquery/${this.state.selectedLocation.id}`)
                console.log(locationResponse.data)
                alert("Sportsko okupljanje uspješno organizirano! Status svojih organiziranih okupljanje možeš provjeriti na stranici svog profila.")
            } catch(err) {
                console.log(err)
            }
            
        } else {
            alert("Molim odaberite lokaciju na karti")
        }
    }

    render() {
        const locationMarkers = this.state.locations.map(l => {
            return l.gpxCoordinates.length === 1 ? 
                <EventMarker
                    key={l.id}
                    location={l}
                    position={l.gpxCoordinates[0]}
                    onMarkerClick={this.selectLocation} 
                /> :
                <EventPolygon 
                    key={l.id}
                    location={l}
                    positions={l.gpxCoordinates}
                    onMarkerClick={this.selectLocation}
                />
        })

        const sportList = this.state.sports.map(sport => <option key={sport.id} value={sport.id}>{sport.name}</option>)

        const selectedLocationBox = this.state.selectedLocation ? <SelectedLocationBox 
                                                            location={this.state.selectedLocation}/> : null

        return(
            <div>
                <div className="event-form">
                    <h1>Organiziraj vlastito sportsko okupljanje</h1>
                    <br/>
                    <label> Sport:
                        <select value={this.state.sport} onChange={this.changeSport}>
                            {sportList}
                        </select>
                    </label>
                </div>
                <form className="event-form">
                    <label> Početak:
                        <input value={this.state.startDate} type="date" name="startDate" onChange={this.handleChange}/>
                        <input value={this.state.startTime} type="time" name="startTime" onChange={this.handleChange}/>
                    </label>
                    <br/>
                    <label> Kraj:
                        <input value={this.state.endDate} type="date" name="endDate" onChange={this.handleChange}/>
                        <input value={this.state.endTime} type="time" name="endTime" onChange={this.handleChange}/>
                    </label>
                    <br/>
                    <label> Maksimalan broj sudionika:
                        <input value={this.state.maxNumberOfParticpents} type="number" name="maxNumberOfParticpents" onChange={this.handleChange}/>
                    </label>
                    <br/>
                    <label>
                        <input 
                            type="radio" 
                            name="sportEventType" 
                            value="free"
                            onChange={this.handleChange}
                            checked={this.state.sportEventType === "free"}
                        /> Besplatno
                    </label>
                    <label>
                        <input 
                            type="radio" 
                            name="sportEventType" 
                            value="paid"
                            onChange={this.handleChange}
                            checked={this.state.sportEventType === "paid"}
                        /> Plaćeno
                    </label>
                    <br/>
                    {this.state.sportEventType === "paid" ? (
                                <label>Cijena(HRK) 
                                    <input value={this.state.cost} type="number" name="cost" onChange={this.handleChange}/>
                                </label>) : null}
                    
                    <hr/>
                    <h3 className="submit-button"
                        onClick={() => this.searchLocations()}>
                        Prikaži lokacije na karti na kojima bi se moglo održati tvoje sportsko okupljanje</h3>
                    <hr/>
                </form>
                <h3 className="event-form">Odaberi lokaciju na karti</h3>
                {selectedLocationBox}
                <Map center={[45.815,15.981]} zoom={12}>
                    <TileLayer
                            attribution='&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors |
                                                Icons made by <a href="https://www.flaticon.com/authors/icongeek26" title="Icongeek26">Icongeek26 </a> 
                                                from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a>'
                                                    
                            url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                    />
                    {locationMarkers}
                </Map>
                <div className="event-form">
                    <h3 className="submit-button"
                            onClick={() => this.submitSportEvent()}>Završi</h3>
                </div>
            </div>
        )
    }
}

export default SportEventForm