#!/usr/bin/env pwsh
# Replace all occurrences of org01_vamsi.vegi with TODO in all .properties files
Get-ChildItem -Path . -Recurse -Include *.properties | ForEach-Object {
    (Get-Content $_.FullName) -replace 'org01_vamsi\.vegi', 'TODO' | Set-Content $_.FullName
    (Get-Content $_.FullName) -replace 'Sanity\#123', 'TODO' | Set-Content $_.FullName
    
    
}
