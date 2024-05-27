import React, {useEffect} from "react";
import {useState} from "react";
import TripTable from './TripTable'
import TripForm from './TripForm'
//import from utils

import {AddTrip, DeleteTrip, GetTrips, updateTrip} from './utils/rest-calls'


import './style/tripForm.css'
import './style/index.css'


export default function TripApp() {


    const [trips, setTrips] = useState([{"place":"Grecia",
        "transportCompanyName":"Fany",
        "departure":"2024-03-03 05:20",
        "price":"50.00",
        "totalSeats":"50",
        "id":"80"
    }
    ]);

    const [selectedTrip, setSelectedTrip] = useState({
        place: '',
        transportCompanyName: '',
        departure: '',
        price: '',
        totalSeats: '',
        id: ''
    });

    const getAll = () => {
        console.log("get all function");
        GetTrips()
            .then(data => {
                setTrips(data);
            })
            .catch(error => console.log('Error fetching trips: ', error));
    };

    // Use useEffect to call getAll when the component mounts
    useEffect(() => {
        getAll();
    }, []);


    function updateFunc(trip){

        console.log("in update trip function",trip.id);
        updateTrip(trip)
            .then(() => {
                // Update the trips state after deletion
                const updatedTrips = trips.filter(t => t.id !== trip.id);
                updatedTrips.push(trip);
                setTrips(updatedTrips);
            })
            .catch(erorr=>console.log('eroare update ',erorr));    }
    function addFunc(trip){
        console.log("in add trip function");
        if(trip.id!=="")
            updateFunc(trip);
        else
        {
            AddTrip(trip)
                .then(res=> GetTrips())
                .then(() =>getAll())
                .catch(erorr=>console.log('eroare add ',erorr));
        }
    }
    function deleteFunc(tripId) {
        console.log("in delete trip function");
        DeleteTrip(tripId)
            .then(() => {
                // Update the trips state after deletion
                const updatedTrips = trips.filter(trip => trip.id !== tripId);
                setTrips(updatedTrips);
            })
            .catch(error => console.log('Error deleting trip: ', error));
        /*console.log("in delete trip function");
        DeleteTrip(tripId)
            .then(res=> GetTrips())
            .then(()=>getAll())
            .catch(error => console.log('Error deleting trip: ', error));*/
    }

    function loadDetailsFunc(tripId) {
        console.log("in load details function");
        const trip = trips.find(t => t.id === tripId);
        console.log(trip);
        if (trip) {
            setSelectedTrip(trip);
        }
        console.log(selectedTrip)

    }

    return (<div className="TripApp">
        <h1> My Management App </h1>
        <TripForm addFunc={addFunc} selectedTrip={selectedTrip}/>
        <br/>
        <br/>
        <TripTable trips={trips} deleteFunc={deleteFunc} loadDetailsFunc={loadDetailsFunc}/>
    </div>);
}