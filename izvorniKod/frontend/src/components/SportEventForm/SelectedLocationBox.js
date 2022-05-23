import axios from 'axios'
import React from 'react'

class SelectedLocationBox extends React.Component {
    render() {
        const locationType = this.props.location.locationStringType === "paid" ? 
                                "Lokaciju se plaća i potrebno ju je rezervirati" : 
                                "Lokaciju je besplatna i nije ju potrebno rezervirati"
        return(
            <div className="location-box">
                <h1>{this.props.location.name}</h1>
                <p>{locationType}</p>
            </div>
        )
    }
}

export default SelectedLocationBox