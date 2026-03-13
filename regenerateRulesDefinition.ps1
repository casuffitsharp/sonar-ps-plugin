$reportFile = Join-path $PSScriptRoot "sonar-ps-plugin\src\main\resources\powershell-rules.xml"
$profileFile = Join-path $PSScriptRoot "sonar-ps-plugin\src\main\resources\powershell-profile.xml"

$xmlProfileWriter = New-Object System.XMl.XmlTextWriter($profileFile , $Null);
# Set The Formatting
$xmlProfileWriter.Formatting = "Indented"
$xmlProfileWriter.Indentation = "4"
    
# Write the XML Declaration
$xmlProfileWriter.WriteStartDocument();
    
# Start Issues XML Element
$xmlProfileWriter.WriteStartElement("profile");
$xmlProfileWriter.WriteElementString("name", "Sonar way");
$xmlProfileWriter.WriteElementString("language", "ps");
$xmlProfileWriter.WriteStartElement("rules");
	
# Create The Document
$xmlWriter = New-Object System.XMl.XmlTextWriter($reportFile , $Null);
    
# Set The Formatting
$xmlWriter.Formatting = "Indented"
$xmlWriter.Indentation = "4"
    
# Write the XML Declaration
$xmlWriter.WriteStartDocument();
    
# Start Issues XML Element
$xmlWriter.WriteStartElement("psrules");

$powershellRules = Get-ScriptAnalyzerRule;
   
foreach ($rule in $powershellRules) { 
    $xmlProfileWriter.WriteStartElement("rule");
    $xmlProfileWriter.WriteElementString("key", $rule.RuleName);
    $xmlProfileWriter.WriteElementString("repositoryKey", "ps-psanalyzer");
    $xmlProfileWriter.WriteEndElement();


    $xmlWriter.WriteStartElement("rule");
    $xmlWriter.WriteElementString("key", $rule.RuleName)
    $xmlWriter.WriteElementString("internalKey", $rule.RuleName)
    $xmlWriter.WriteElementString("name", $rule.CommonName)
    $xmlWriter.WriteElementString("description", $rule.Description)
    $xmlWriter.WriteElementString("cardinality", "SINGLE")
    $xmlWriter.WriteElementString("remediationFunction", "LINEAR")
    $xmlWriter.WriteElementString("descriptionFormat", "HTML")
			
    $xmlWriter.WriteElementString("remediationFunctionBaseEffort", "")
    $remediationDefaultTime = "2min";
    $severity = "INFO";
    $ruleType = "CODE_SMELL";
        
    if ($rule.Severity -eq "Information") {
        $severity = "INFO";
        $remediationDefaultTime = "2min";
    }
    if ($rule.Severity -eq "Warning") {
        $severity = "MAJOR";
        $remediationDefaultTime = "5min";
    }
    if ($rule.Severity -eq "ERROR") {
        $severity = "BLOCKER";
        $remediationDefaultTime = "15min";
        $ruleType = "BUG";
    }

    $cleanCodeAttribute = "CONVENTIONAL";
    $softwareQualityImpact = "MAINTAINABILITY";

    if ($rule.RuleName -match "Password|Credential|InvokeExpression|SecureString|PlainText|Injection|Authentication|BrokenHash|Hardcoded") {
        $ruleType = "VULNERABILITY";
        $cleanCodeAttribute = "TRUSTWORTHY";
        $softwareQualityImpact = "SECURITY";
    }
    elseif ($rule.RuleName -match "IncorrectUsage|Compatible|Reserved|EmptyMember|NullOrEmpty") {
        $ruleType = "BUG";
        $cleanCodeAttribute = "LOGICAL";
        $softwareQualityImpact = "RELIABILITY";
    }
    
    # Refine Clean Code Attributes
    if ($rule.RuleName -match "Compatible|Deprecated|ModuleToProcess") {
        $cleanCodeAttribute = "MODULAR";
    }
    elseif ($rule.RuleName -match "Complexity|Long|Unused") {
        $cleanCodeAttribute = "FOCUSED";
    }

    # Ensure BUG-type rules default to RELIABILITY if not already set by keyword matching
    if ($ruleType -eq "BUG" -and $softwareQualityImpact -eq "MAINTAINABILITY") {
        $softwareQualityImpact = "RELIABILITY";
        if ($cleanCodeAttribute -eq "CONVENTIONAL") {
            $cleanCodeAttribute = "LOGICAL";
        }
    }

    $xmlWriter.WriteElementString("debtRemediationFunctionCoefficient", $remediationDefaultTime);
    $xmlWriter.WriteElementString("severity", $severity);
    $xmlWriter.WriteElementString("type", $ruleType);
    $xmlWriter.WriteElementString("cleanCodeAttribute", $cleanCodeAttribute);
    $xmlWriter.WriteElementString("softwareQualityImpact", $softwareQualityImpact);
    $xmlWriter.WriteEndElement();
}

# End Issues XML element
$xmlWriter.WriteEndElement();
    
# End the XML Document
$xmlWriter.WriteEndDocument();
    
# Finish The Document
$xmlWriter.Flush()
$xmlWriter.Close();

$xmlProfileWriter.WriteEndDocument();	

# Finish The Document
$xmlProfileWriter.Flush()
$xmlProfileWriter.Close();