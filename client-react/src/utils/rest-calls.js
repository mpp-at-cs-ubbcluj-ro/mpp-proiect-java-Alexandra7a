
import {AGENCY_TRIP_BASE_URL} from './consts';

/**
 * chech if the promise was succesful or not(made function to avoid dublicate code)*/
function status(response) {
    console.log('response status '+response.status);
    if (response.status >= 200 && response.status < 300) {
        return Promise.resolve(response)
    } else {
        return Promise.reject(new Error(response.statusText))
    }
}

function json(response) {
    return response.json()
}



export function GetTrips(){
    let headers = new Headers();
    headers.append('Accept', 'application/json');
    let myInit = { method: 'GET',
        headers: headers,
        mode: 'cors'
    };

    let request = new Request(AGENCY_TRIP_BASE_URL, myInit);

    console.log('Inainte de fetch GET pentru '+AGENCY_TRIP_BASE_URL)

    return fetch(request)
        .then(status)
        .then(json)
        .then(data=> {
            console.log('Request succeeded with JSON response', data);
            return data;
        }).catch(error=>{
            console.log('Request failed', error);
            return Promise.reject(error);
        });
}



export function AddTrip(trip) {
    console.log('inainte de fetch post' + JSON.stringify(trip));
    //trip.departure = Date.parse(trip.departure.toString());

    //define headers here
    let myHeaders = new Headers();
    myHeaders.append("Accept", "application/json");
    myHeaders.append("Content-Type", "application/json");

    //create the request at the rest services
    let antet = {
        method: 'POST',
        headers: myHeaders,
        mode: 'cors', // to avoid CORS policy error
        body: JSON.stringify(trip)
    };

    return fetch(AGENCY_TRIP_BASE_URL, antet)
        .then(status)
        .then(response => {
            return response.text();
        }).catch(error => {
            console.log('Request ADD failed', error);
            return Promise.reject(error);
        });
    /**
     * STRUCTURA DE PROMISE:
     * */

}
    export function DeleteTrip(id){

        console.log('inainte de fetch delete')
        let myHeaders = new Headers();
        myHeaders.append("Accept", "application/json");

        let antet = { method: 'DELETE',
            headers: myHeaders,
            mode: 'cors'
        };

        const URL=AGENCY_TRIP_BASE_URL+'/'+id;
        console.log('URL pentru delete   '+URL)
        return fetch(URL,antet)
            .then(status)
            .then(response=>{
                console.log('Delete status '+response.status);
                return response.text();
            }).catch(e=>{
                console.log('error '+e);
                return Promise.reject(e);
            });
    }

    export function updateTrip(trip){
        console.log('inainte de fetch post' + JSON.stringify(trip));
        //trip.departure = Date.parse(trip.departure.toString());

        //define headers here
        let myHeaders = new Headers();
        myHeaders.append("Accept", "application/json");
        myHeaders.append("Content-Type", "application/json");

        //create the request at the rest services
        let antet = {
            method: 'PUT',
            headers: myHeaders,
            mode: 'cors', // to avoid CORS policy error
            body: JSON.stringify(trip)
        };

        const URL=AGENCY_TRIP_BASE_URL+'/'+trip.id;
        console.log(URL);
        return fetch(URL, antet)
            .then(status)
            .then(response => {
                return response.text();
            }).catch(error => {
                console.log('Request ADD failed', error);
                return Promise.reject(error);
            });
    }





