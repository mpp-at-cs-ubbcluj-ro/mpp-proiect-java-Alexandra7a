import React, {useEffect} from "react";
import {useState} from "react";



export default function TripApp({addFunc,selectedTrip}) {

    const [place, setPlace] = useState('');
    const [transportCompanyName, setTransportCompanyName] = useState('');
    const [departure, setDeparture] = useState('');
    const [price, setPrice] = useState('');
    const [totalSeats, setTotalSeats] = useState('');
    const [id, setId] = useState();


    useEffect(() => {
        if (selectedTrip) {
            setPlace(selectedTrip.place);
            setTransportCompanyName(selectedTrip.transportCompanyName);
            setDeparture(selectedTrip.departure);
            setPrice(selectedTrip.price);
            setTotalSeats(selectedTrip.totalSeats);
            setId(selectedTrip.id);
        }
    }, [selectedTrip]);

    function handleSubmit (event){

        let trip={
            place : place,
            transportCompanyName : transportCompanyName,
            departure : departure,
            price : price,
            totalSeats : totalSeats,
            id:id
        }


        console.log('A trip was submitted: ');
        console.log(trip);
        addFunc(trip);
        event.preventDefault(); // to not empty the form after submission
    }
    function resetForm() {
        setPlace('');
        setTransportCompanyName('');
        setDeparture('');
        setPrice('');
        setTotalSeats('');
        setId('');
    }
    return(
        <form onSubmit={handleSubmit} onReset={resetForm}>
            <label>
                Id :
                <input  type="text" value={id} onChange={e=>setPlace(e.target.value)} disabled={true}/>
            </label><br/>
            <label>
                Place :
                <input type="text" value={place} onChange={e=>setPlace(e.target.value)} />
            </label><br/>
            <label>
                Transport Company Name:
                <input type="text" value={transportCompanyName} onChange={e=>setTransportCompanyName(e.target.value)} />
            </label><br/>
            <label>
                Departure:
                <input type="datetime-local" value={departure} onChange={e=>setDeparture(e.target.value)} />
            </label><br/>
            <label>
                Price:
                <input type="text" value={price} onChange={e=>setPrice(e.target.value)} />
            </label><br/>
            <label>
                No Seats:
                <input type="number" value={totalSeats} onChange={e=>setTotalSeats(e.target.value)} />
            </label><br/>

            <input type="submit" value="Add new trip" />
            <input type="reset" value="Empty form "/>
        </form>);


}