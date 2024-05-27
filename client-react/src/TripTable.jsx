
import React from  'react';
import './style/table.css'
import './TripApp'
function TripRow({trip, deleteFunc,loadDetailsFunc}){

    function handleLoad(event){
        console.log('load details fun '+trip.id);
        loadDetailsFunc(trip.id);

    }
    function handleDelete(event){
        console.log('delete button pentru '+trip.id);
        deleteFunc(trip.id);
    }
    return (
        <tr onClick={handleLoad}>
            <td>{trip.id}</td>
            <td>{trip.place}</td>
            <td>{trip.transportCompanyName}</td>
            <td>{trip.departure}</td>
            <td>{trip.price}</td>
            <td>{trip.totalSeats}</td>
            <td><button  onClick={handleDelete}>Delete</button></td>
        </tr>
    );
}


export default function tripTable({trips, deleteFunc,loadDetailsFunc}){


    console.log("In Table");
    console.log(trips);
    let rows = [];
    // let functieStergere=deleteFunc;
    trips.forEach(function(trip) {
        rows.push(<TripRow trip={trip}  key={trip.id} deleteFunc={deleteFunc} loadDetailsFunc={loadDetailsFunc} />);
    });
    return (
        <div className="container-table">

            <table className="trip-table">
                <thead>
                <tr>
                    <th>Id</th>
                    <th>Place</th>
                    <th>Transport Company</th>
                    <th>Departure</th>
                    <th>Price</th>
                    <th>NoSeats</th>
                </tr>
                </thead>
                <tbody>{rows}</tbody>
            </table>

        </div>
    );
}