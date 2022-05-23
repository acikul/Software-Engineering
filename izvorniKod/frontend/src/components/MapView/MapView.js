import React from 'react'
import { Link } from 'react-router-dom'

import axios from 'axios'

import {Map, TileLayer } from 'react-leaflet'

import './MapView.css'

import EventMarker from './EventMarker'
import EventPolygon from './EventPolygon'
import SportEventBox from './SportEventBox'
import SelectedSportEventBox from './SelectedSportEventBox'
import SportCheckBoxes from './SportCheckboxes'
import SportEventForm from '../SportEventForm/SportEventForm'

import SportService from '../authServices/SportService'
import MapService from '../authServices/MapService'
import LocationService from '../authServices/LocationService'


class MapView extends React.Component {
    constructor() {
        super()
        this.state = {
            currentLocation: { lat: 45.815, lng: 15.981 }, //Koordiante Zagreba
            zoom: 12,
            eventsData: [], //popis svih okupljanja
            selectedEventsData: [], //popis okupljanja za koje vrijede odabrani filteri
            clickedEvents: [], //okupljanja koja su kliknuta
            sports: [], //popis svih sportova
            selectedSports: {}, // sport odabran ili ne, npr.{handball:true, football:false}
            date: '',   //datum (za filter)
            sportIcons: {}, //ikonice za sportove
            locations: [],  //popis svih lokacija
            selectedLocations: [], //popis lokacija za koje vrijede odabarni filteri
        }

        this.eventSelect = this.eventSelect.bind(this)
        this.handleChange = this.handleChange.bind(this)
        this.recommendEvent = this.recommendEvent.bind(this)
    }

    async componentDidMount() {
        const events = await MapService({   //defaultna lokacija Zagreba
                                            topRightLatitude:45.86228122137575,
                                            topRightLongitude:16.115913391113285,
                                            bottomLeftLatitude:45.766564985445,
                                            bottomLeftLongitude:15.846405029296877,
                                        })
                                        
        if(events) {
            let locations = []      //dohvaćanje svih lokacija za koje postoji dohvaćeno sportsko okupljanje
            let done = []
            for (let i = 0; i<events.length; i++) {
                if (!done.includes(events[i].location)) {
                    let location = await LocationService(events[i].location)
                    location.events = []
                    locations.push(location)
                    done.push(events[i].location)
                }
                for (let j = 0; j<locations.length; j++) {
                    if (events[i].location === locations[j].id) {
                        locations[j].events.push(events[i])
                    }
                }
            }
            console.log(locations)
            this.setState({eventsData:events, selectedEventsData:events, locations:locations, selectedLocations:locations})
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


    handleChange(event) {
        const { type, value, checked } = event.target

        if (type === "checkbox") {          //filtriranje na temelju odabarnih sportova
            this.setState(prevState => {
                const newSelectedSports = prevState.selectedSports
                newSelectedSports[value] = checked

                const filteredLocations = this.state.locations.filter(l => {
                    for(let i = 0; i<l.events.length; i++) {
                        if (newSelectedSports[l.events[i].sport] === true) {
                            return true
                        }
                    }
                    return false
                })
                const filteredEventsData = this.state.eventsData.filter(sevent => newSelectedSports[sevent.sport] === true)

                return {selectedSports:newSelectedSports, selectedEventsData:filteredEventsData, selectedLocations:filteredLocations}
            })
        } else if(type === "date") {        //filtriranje na temelju datuma
            value ? 
            this.setState(prevState => {
                const filteredLocations = this.state.locations.filter(l => {
                    for(let i = 0; i<l.events.length; i++) {
                        if (l.events[i].startDateTime.split("T")[0] === value) {
                            return true
                        }
                    }
                    return false
                })
                const filteredEventsData = this.state.eventsData.filter(sevent => sevent.startDateTime.split("T")[0] === value)
                return {date:value, selectedEventsData:filteredEventsData, selectedLocations:filteredLocations}
            }) :
            this.setState(prevState => {
                return {date:value, selectedEventsData:this.state.eventsData, selectedLocations:this.state.locations}
            })
        }
    }

    async SendCurrentLocation() {        //slanje trenutnih okvira karte kako bi se dobila sportska okupljanja
        const pos = this.map.leafletElement.getBounds()
        const newPosition = {
            topRightLatitude:pos._northEast.lat,
            topRightLongitude:pos._northEast.lng,
            bottomLeftLatitude:pos._southWest.lat,
            bottomLeftLongitude:pos._southWest.lng,
        }
        const events = await MapService(newPosition)
        

        if(events) {                     
            let locations = []                  //dohvaćanje svih lokacija za koje postoji dohvaćeno sportsko okupljanje
            let done = []   
            for (let i = 0; i<events.length; i++) {
                if (!done.includes(events[i].location)) {
                    let location = await LocationService(events[i].location)
                    location.events = []
                    locations.push(location)
                    done.push(events[i].location)
                }
                for (let j = 0; j<locations.length; j++) {
                    if (events[i].location === locations[j].id) {
                        locations[j].events.push(events[i])
                    }
                }
            }

            //filtriranje novodobivenih podataka
            const filteredEventsData = events.filter(sevent => this.state.selectedSports[sevent.sport] === true)
            const filteredLocations = this.state.locations.filter(l => {
                for(let i = 0; i<l.events.length; i++) {
                    if (this.state.selectedSports[l.events[i].sport] === true) {
                        return true
                    }
                }
                return false
            })
            this.setState({eventsData:events, selectedEventsData:filteredEventsData, locations:locations, selectedLocations:filteredLocations})
        }  
        
    }

    eventSelect = (events) => {          //odabir sportskih okupljanja o kojima će se prikazati detalji
        if (Array.isArray(events)) {
            this.setState({clickedEvents: events})
        } else {
            this.setState({clickedEvents: [events]})
        }
    }

    async recommendEvent() {        //preporuka sporta na temelju korisnikovih osobina
        try {
            const recommendSport = await axios.get(`/v1/users/${this.props.user.user_info.id}/recommend_sport`)

            this.setState(prevState => {
                let newSelectedSports = prevState.selectedSports
                for(let sport in newSelectedSports) {
                    if (sport === recommendSport.data.name) {
                        newSelectedSports[sport] = true
                    } else {
                        newSelectedSports[sport] = false
                    }
                }

                const filteredLocations = this.state.locations.filter(l => {
                    for(let i = 0; i<l.events.length; i++) {
                        if (newSelectedSports[l.events[i].sport] === true) {
                            return true
                        }
                    }
                    return false
                })
                const filteredEventsData = this.state.eventsData.filter(sevent => newSelectedSports[sevent.sport] === true)

                return {selectedSports:newSelectedSports, selectedEventsData:filteredEventsData, selectedLocations:filteredLocations}
            })
        } catch(err) {
            console.log(err)
        }
    }

    render() {
        const locationMarkers = this.state.selectedLocations.map(l => {
            return l.gpxCoordinates.length === 1 ? 
                <EventMarker
                    key={l.id}
                    position={l.gpxCoordinates[0]}
                    events={l.events}
                    onMarkerClick={this.eventSelect} 
                /> :
                <EventPolygon 
                    key={l.id}
                    positions={l.gpxCoordinates}
                    events={l.events}
                    onMarkerClick={this.eventSelect}
                />
        })
        
        const eventList = this.state.selectedEventsData.map(sevent => <SportEventBox
                                                                icon={this.state.sportIcons[sevent.sport]}
                                                                key = {sevent.id} 
                                                                sevent = {sevent}
                                                                handleClick={this.eventSelect}
                                                                />)
        
        const sportCheckBoxes = this.state.sports.map(sport => <SportCheckBoxes
                                                                    key={sport.id}
                                                                    checked={this.state.selectedSports[sport.name]}
                                                                    handleChange={this.handleChange}
                                                                    value={sport.name}
                                                                    />)
        
        const {currentLocation, zoom } = this.state

        const selectedSportEventBoxData = this.state.clickedEvents.map(sevent =>
                                        <SelectedSportEventBox
                                            user={this.props.user} 
                                            key={sevent.id}
                                            sevent={sevent}
                                            icon={this.state.sportIcons[sevent.sport]} 
                                        />) 
        

        return(
            <div className="map-view">
                <Map ref={(ref) => {this.map = ref}} center={currentLocation} zoom={zoom}
                    onMoveEnd={() => this.SendCurrentLocation()}>
                    {/*<MapConsumer>
                            {() => {
                                const map = useMapEvent('moveend', () => { this.SendCurrentLocation(map.getBounds()) })
                                return null
                            }}
                    </MapConsumer>*/}
                    <TileLayer
                        attribution='&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors |
                                            Icons made by <a href="https://www.flaticon.com/authors/icongeek26" title="Icongeek26">Icongeek26 </a> 
                                            from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a>'
                                                
                        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                    />
                    {/*{eventMarkers}*/}
                    {locationMarkers}
                </Map>

                <form className="event-filter">
                    {sportCheckBoxes}
                    {this.props.user ? <h3 className="recommend-button" onClick={()=>this.recommendEvent()}>Preporuči mi sport</h3> : null}
                    <br/>
                    <label> Datum
                        <input 
                            type="date"
                            onChange={this.handleChange}
                            value={this.state.date}
                        />
                    </label>
                    
                    <hr />
                    {this.props.user ? <Link to='/stvoriokupljanje' onClick={ () => <SportEventForm user={this.state.user} /> } >
                                            <h3 className="recommend-button">Organiziraj vlastito sportsko okupljanje</h3>
                                        </Link> 
                                        : "Nisi prijavljen(a)"}
                    
                </form>

                {selectedSportEventBoxData}

                <hr />
                
                <div className="event-list">
                    {eventList}
                </div>

                <hr/>
            </div>
        )
    }
}

export default MapView