#!/bin/bash

set -e
cd "$(dirname "$0")"

actionName=$1
appName=$2
cidrPrefix=$3

function checkAppName() {
    regexAppName="^[0-9a-z\-]+$";
    if [[ ! "$appName" =~ $regexAppName ]]; then
        echo "Application name not matching regex $regexAppName"
        exit 1
    fi

    if grep -q "${appName}" ./config/*; then
        echo "Application name already used"
        exit 1
    fi
}

function checkCIDRPrefix() {
    regexCIDRPrefix="^[0-9]+\.[0-9]+$";
    if [[ ! "$cidrPrefix" =~ $regexCIDRPrefix ]]; then
        echo "CIDR prefix not matching regex $regexCIDRPrefix"
        exit 1
    fi

    if grep -q "${cidrPrefix}" ./config/*; then
        echo "CIDR prefix already used"
        exit 1
    fi
}

function switchWorkspace() {
    configFile=./config/${appName}.tfvars
    if [[ ! -f "$configFile" ]]; then
        echo "Configuration file '$configFile' doesn't exist"
        exit 1
    fi
    terraform workspace select ${appName}
}

function createInfrastructure() {
    checkAppName
    checkCIDRPrefix

    printf "appName = \"$appName\"\ncidrPrefix = \"$cidrPrefix\"" > "./config/$appName.tfvars"
    git add ./config/${appName}.tfvars
    terraform workspace new ${appName}

    updateInfrastructure
}

function updateInfrastructure() {
    switchWorkspace
    terraform apply -auto-approve -var-file=./config/${appName}.tfvars

    git add ./terraform.tfstate.d/${appName}/.
}

function destroyInfrastructure() {
    switchWorkspace
    terraform destroy -auto-approve -var-file=./config/${appName}.tfvars
}

function destroyAllInfrastructure() {
    destroyInfrastructure

    terraform workspace select default
    terraform workspace delete ${appName}

    rm -rf ./terraform.tfstate.d/${appName}/
    rm -f ./config/${appName}.tfvars
}

if [[ "$actionName" == "create" ]]; then
    createInfrastructure
    echo "INFRASTRUCTURE CREATED SUCCESSFULLY"
elif [[ "$actionName" == "update" ]]; then
    updateInfrastructure
    echo "INFRASTRUCTURE UPDATED SUCCESSFULLY"
elif [[ "$actionName" == "destroy" ]]; then
    destroyInfrastructure
    echo "INFRASTRUCTURE DESTROYED"
elif [[ "$actionName" == "destroyAll" ]]; then
    destroyAllInfrastructure
    echo "INFRASTRUCTURE ALL DESTROYED"
else
    echo "Unknown action $actionName"
    exit 1
fi
