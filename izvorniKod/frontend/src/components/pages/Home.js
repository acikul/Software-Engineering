import '../../App.css'
import HomeSection from '../HomeSection/HomeSection'
import React from 'react'


function Home(props) {
    return (
        <>
            <HomeSection user={props.user} />
        </>
    )
}

export default Home;
