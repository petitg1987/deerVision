# Pre-requisites
## Server setup
* Set up a server using 'serverSetup' project
* Create a file `~/.vault_pass.txt` with the Ansible vault password
* Update the IP address of the server in 'infra/inventory.ini'
* Execute: `ansible-playbook -i setup/infra/inventory.ini setup/infra/site.yml --vault-password-file ~/.vault_pass.txt`

## Configure GitHub
* Define a webhook in 'deerVision' repository settings:
  * Payload URL: http://152.53.144.87:11001
  * Content type: application/json
  * Events: push

## Load database from a backup dump
* Unzip backup file 'deervision_dump.sql' in home folder
* Execute: `scp deervision_dump.sql greg@152.53.144.87:/tmp/deervision_dump_to_restore.sql`
* Execute: `ssh greg@152.53.144.87 "cat /tmp/deervision_dump_to_restore.sql | sudo docker exec -i deer-vision-db psql -U postgres -d postgres"`
* Check: `ssh greg@152.53.144.87 "sudo docker exec -i deer-vision-db psql -U postgres -d postgres -c 'SELECT * FROM usage ORDER BY id DESC LIMIT 1;'"`

# Deploy the application
* Commit on master branch to trigger the webhook

# Utils
## Connect to the server
* Execute: `ssh greg@152.53.144.87`

## Decrypt secret from the Ansible vault
* Execute:
    ```
    export chiphertext='$ANSIBLE_VAULT;1.1;AES256
    61376537306166376333633238636535663563663434633239363162653065383934326362393365
    3165303132666138313935393532336539353661373136330a646436353032633631383134323137
    33326438613765663664363331633130326565633331383034393564373165326462613861393232
    6662366239363466640a323634633038653939303965316166396161653363623437313531333535
    3161'
    printf "%s\n" $chiphertext | ansible-vault decrypt
    ````