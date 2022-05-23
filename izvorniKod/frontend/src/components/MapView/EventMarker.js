import React from 'react'
import { Marker } from 'react-leaflet'

class EventMarker extends React.Component {
    render() {
        return (
            <Marker
                position={this.props.position}
                onClick = {() => { this.props.events ?
                                    this.props.onMarkerClick(this.props.events) :
                                    this.props.onMarkerClick(this.props.location) }} >
            </Marker>
        )
    }
}

export default EventMarker