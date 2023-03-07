import React, {Component} from 'react';
import './total-player.css';
import {getWithToken} from "../../js/request";
import {getBackendUrl} from "../../js/access";

class TotalPlayer extends Component {

    async componentDidMount() {
        let ulTotalPlayer = document.getElementById("listTotalPlayers");
        let totalRequestKeyJson = await getWithToken(getBackendUrl() + 'api/admin/usage/total-request-keys', this.props.token);
        for (let appNameKey in totalRequestKeyJson) {
            let appName = appNameKey
                .replace(/([A-Z])/g, ' $1') //insert a space before all caps
                .replace(/^./, (str) => str.toUpperCase());

            let liElement = document.createElement("li");
            liElement.innerHTML = appName + ": <em>" + totalRequestKeyJson[appNameKey] + "</em>";
            ulTotalPlayer.appendChild(liElement);
        }
    }

    render() {
        return (
            <div className="totalPlayers">
                Unique players:
                <ul id="listTotalPlayers" />
            </div>
        );
    }
}

export default TotalPlayer;