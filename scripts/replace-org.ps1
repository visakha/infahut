#!/usr/bin/env pwsh

# Replace informatica.username and informatica.password with fixed values in all .properties files
Get-ChildItem -Path . -Recurse -Include *.properties | ForEach-Object {
    (Get-Content $_.FullName) -replace 'informatica\.username\s*=.*', 'informatica.username=user123' |
        ForEach-Object { $_ -replace 'informatica\.password\s*=.*', 'informatica.password=pwd123' } |
        Set-Content $_.FullName
}
