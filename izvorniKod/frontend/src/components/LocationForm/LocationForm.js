import React from 'react'
import { Map, TileLayer, FeatureGroup } from 'react-leaflet'
import { EditControl } from 'react-leaflet-draw'

import axios from 'axios'

import './LocationForm.css'

class LocationForm extends React.Component {
    constructor() {
        super()
        this.state = {
            position: "",
            locationName: "",
            locationType: "free"
        }
        this.onCreated = this.onCreated.bind(this)
        this.onEdited = this.onEdited.bind(this)
        this.onDeleted = this.onDeleted.bind(this)
        this.handleChange = this.handleChange.bind(this)
    }

    handleChange(event) {
        const {name, value, type, checked} = event.target
        this.setState({[name]:value})

    }

    onCreated = (e) => {
        const pos = e.layer._latlngs[0]
        let newPosition = "["
        for(let i = 0; i<pos.length; i++) {
            newPosition += "["+pos[i].lat.toFixed(6)+","+pos[i].lng.toFixed(6)+"]"
            if (i!= pos.length-1) {
                newPosition += ","
            }
        }
        newPosition += "]"

        this.setState({position:newPosition})

        console.log(newPosition)
    }

    onEdited = (e) => {
        let pos
        e.layers.eachLayer(a => {
            pos = a.toGeoJSON()
        });
        let newPosition = "["
        pos = pos.geometry.coordinates[0]
        for(let i = 0; i<pos.length-1; i++) {
            newPosition += "["+pos[i][1]+","+pos[i][0]+"]"
        }
        newPosition += "]"

        this.setState({position:newPosition})

        console.log(newPosition)
    }

    onDeleted = (e) => {
        const newPosition = ""
        this.setState({position:newPosition})
        console.log(newPosition)
    }

    async submitLocation()  {
        const location = {
            name: this.state.locationName,
            locationType: this.state.locationType,
            gpxCoordinates: this.state.position
        }
        console.log(location)
        try {
            const response = await axios.post(`/v1/users/${this.props.user.user_info.id}/locations`,location)
            alert("Lokacija uspješno stvorena! Status svojih lokacija možeš provjeriti na stranici svog profila. Za plaćene lokacije moraš priložiti odgovarajuću dokumentaciju prije nego što se mogu iznajmiti")
        } catch(err) {
            console.log(err)
            alert("Došlo je do pogreške")
        }


    }

    render() {
        return (
            <div className="map-view">
                <p className="location-form">Ucrtaj svoju lokaciju u kartu</p>
                <Map center={[45.815,15.981]} zoom={12}>
                    <TileLayer
                        attribution='&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors |
                                            Icons made by <a href="https://www.flaticon.com/authors/icongeek26" title="Icongeek26">Icongeek26 </a> 
                                            from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a>'
                                                
                        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                    />
                    <FeatureGroup >
                        <EditControl
                            position="topright"
                            onCreated={this.onCreated}
                            onEdited={this.onEdited}
                            onDeleted={this.onDeleted}
                            draw={{
                                marker:false,
                                circle:false,
                                rectangle:false,
                                polyline:false,
                                circlemarker:false,
                            }}
                        />
                    </FeatureGroup>
                </Map>
                <form className="location-form">
                    <label for="lname">Ime lokacije: </label>
                    <input 
                        type="text" 
                        name="locationName" 
                        value={this.state.locationName}
                        onChange={this.handleChange}
                    />
                    <br/>
                    <label>Tip lokacije: </label>
                        <label>
                            <input 
                                type="radio" 
                                name="locationType" 
                                value="free"
                                onChange={this.handleChange}
                                checked={this.state.locationType === "free"}
                            /> Besplatna
                        </label>
                        <label>
                            <input 
                                type="radio" 
                                name="locationType" 
                                value="paid"
                                onChange={this.handleChange}
                                checked={this.state.locationType === "paid"}
                            /> Plaćena
                        </label>
                        <br/>
                        <h3 className="submit-button"
                            onClick={() => this.submitLocation()}>Završi</h3>
                </form>
            </div>
        )
    }
}

export default LocationForm