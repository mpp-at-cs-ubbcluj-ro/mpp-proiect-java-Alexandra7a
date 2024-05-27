import React from 'react'
import ReactDOM from 'react-dom/client'
import TripApp from './TripApp.jsx';

ReactDOM.createRoot(document.getElementById('root')).render(
    //<React.StrictMode>
    <TripApp />
    // </React.StrictMode>,
)

/*



const container=document.getElementById('root');
const root=createRoot(container);
root.render( <UserApp/>);

ReactDOM.render(
  <div>
 <UserApp/>
  </div>,
  document.getElementById('root')
);*/
