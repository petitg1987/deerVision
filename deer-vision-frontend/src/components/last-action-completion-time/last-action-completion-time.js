import React, {Component} from 'react';
import './last-action-completion-time.css';
import {getWithToken} from "../../js/request";
import {getBackendUrl} from "../../js/access";

class LastActionCompletionTime extends Component {

    constructor(props) {
        super(props);
    }

    async refreshLevelIds() {
        let levelsSelector = document.getElementById("levelsSelect");
        let levelIdsJson = await getWithToken(getBackendUrl() + 'api/admin/levels/ids?appId=' + this.props.appId, this.props.token);

        let selectedLevelStillValid = false;

        levelIdsJson.forEach(levelNumber => {
            let option = document.createElement("option");
            option.text = "Level " + levelNumber;
            option.value = levelNumber;
            levelsSelector.add(option);

            if (levelNumber === this.state.levelSelected) {
                selectedLevelStillValid = true;
                option.selected = true;
            }
        });

        if (!selectedLevelStillValid) {
            levelsSelector.selectedIndex = 0;
            this.setState({levelSelected: levelsSelector.value}, this.refreshActionNames);
        } else {
            await this.refreshActionNames();
        }
    }

    async refreshActionNames() {
        let actionNamesSelector = document.getElementById("actionNamesSelect");
        let actionNamesJson = await getWithToken(getBackendUrl() + 'api/admin/levels/' + this.state.levelSelected + '/actionNames?appId=' + this.props.appId, this.props.token);

        let selectedActionNameStillValid = false;

        actionNamesSelector.length = 0;
        actionNamesJson.forEach(actionName => {
            let option = document.createElement("option");
            option.text = actionName.replace(/([A-Z])/g, ' $1').trim();
            option.value = actionName;
            actionNamesSelector.add(option);

            if (actionName === this.state.actionNameSelected) {
                selectedActionNameStillValid = true;
                option.selected = true;
            }
        });

        if (!selectedActionNameStillValid) {
            actionNamesSelector.selectedIndex = 0;
            this.setState({actionNameSelected: actionNamesSelector.value}, this.refreshActionNames);
        } else {
            this.refreshChart().then(() => {});
        }
    }

    async componentDidMount() {
        await this.refreshLevelIds();
    }

    render() {
        return (
            <div className="actionCompletionTimeChart">
                LOL
            </div>
        );
    }
}

export default LastActionCompletionTime;