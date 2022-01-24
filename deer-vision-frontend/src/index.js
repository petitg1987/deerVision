import React, {Component, Suspense} from 'react';
import ReactDOM from 'react-dom';
import {BrowserRouter, Routes, Route} from 'react-router-dom';
import Navigation from "./components/navigation/navigation";
import Home from './pages/home/home';
import PrivacyPolicy from "./pages/privacy/privacy";
import './index.css';

const Admin = React.lazy(() => import('./pages/admin/admin'))

class Index extends Component {

    constructor(props) {
        super(props);

        let date = new Date();
        this.currentYear = date.getFullYear();
    }

    render() {
        return (
            <BrowserRouter>
                <div>
                    <header>
                        <Navigation/>
                    </header>
                    <div className="content">
                        <Suspense fallback={<div>Loading...</div>}>
                            <Routes>
                                <Route exact path='/' element={<Home/>}/>
                                <Route exact path='/admin' element={<Admin/>}/>
                                <Route exact path='/privacy-policy' element={<PrivacyPolicy/>}/>
                            </Routes>
                        </Suspense>
                    </div>
                    <footer>
                        <center><br/><small>&copy; Copyright {this.currentYear}, Deer Vision Studio | <a className={"text-link"} href={"/privacy-policy"}>Privacy Policy</a></small><br/><br/></center>
                    </footer>
                </div>
            </BrowserRouter>
        );
    }
}

ReactDOM.render((
    <Index/>
), document.getElementById('app'))
