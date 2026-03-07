param( 
	[string]$inputFile,
	[string]$output,
	[int] $depth = 9999999
)

$text = ([IO.File]::ReadAllText($inputFile)) -replace "\xEF\xBB\xBF", "";
$tokens = $null
$errors = $null
$ast = [Management.Automation.Language.Parser]::ParseInput($text , [ref]$tokens, [ref]$errors);

$complexity = 1;
$switches = $ast.FindAll({$args[0] -is [System.Management.Automation.Language.SwitchStatementAst]}, $true)

Foreach ( $item in $switches ) { 
    $complexity += $item.Clauses.Count
}

$tryCatches = $ast.FindAll({$args[0] -is [System.Management.Automation.Language.TryStatementAst]}, $true)

Foreach ( $item in $tryCatches ) { 
    $complexity += $item.CatchClauses.Count
}

$ifs = $ast.FindAll({$args[0] -is [System.Management.Automation.Language.IfStatementAst]}, $true)

Foreach ( $item in $ifs ) {
    $complexity += 2
}

$binaryExpressions = $ast.FindAll({$args[0] -is [System.Management.Automation.Language.BinaryExpressionAst]}, $true)

Foreach ( $item in $binaryExpressions ) {
   
    $complexity += 1
}

$whileStatements = $ast.FindAll({$args[0] -is [System.Management.Automation.Language.WhileStatementAst]}, $true)
Foreach ( $item in $whileStatements ) {
    $complexity += 1
}

$xmlWriter = New-Object System.XMl.XmlTextWriter($output , $Null);
$xmlWriter.WriteStartDocument();
$xmlWriter.WriteStartElement("Tokens");
$xmlWriter.WriteAttributeString("complexity", $complexity);

function Get-NestingLevel {
    param([System.Management.Automation.Language.Ast]$Node)
    $level = 0
    $parent = $Node.Parent
    while ($null -ne $parent) {
        if ($parent -is [System.Management.Automation.Language.IfStatementAst] -or
            $parent -is [System.Management.Automation.Language.SwitchStatementAst] -or
            $parent -is [System.Management.Automation.Language.ForStatementAst] -or
            $parent -is [System.Management.Automation.Language.ForEachStatementAst] -or
            $parent -is [System.Management.Automation.Language.WhileStatementAst] -or
            $parent -is [System.Management.Automation.Language.DoWhileStatementAst] -or
            $parent -is [System.Management.Automation.Language.DoUntilStatementAst] -or
            $parent -is [System.Management.Automation.Language.CatchClauseAst]) {
            $level++
        }
        $parent = $parent.Parent
    }
    return $level
}

$cognitiveComplexity = 0

$incrementingNodes = $ast.FindAll({
    $args[0] -is [System.Management.Automation.Language.IfStatementAst] -or
    $args[0] -is [System.Management.Automation.Language.SwitchStatementAst] -or
    $args[0] -is [System.Management.Automation.Language.ForStatementAst] -or
    $args[0] -is [System.Management.Automation.Language.ForEachStatementAst] -or
    $args[0] -is [System.Management.Automation.Language.WhileStatementAst] -or
    $args[0] -is [System.Management.Automation.Language.DoWhileStatementAst] -or
    $args[0] -is [System.Management.Automation.Language.DoUntilStatementAst] -or
    $args[0] -is [System.Management.Automation.Language.CatchClauseAst]
}, $true)

foreach ($node in $incrementingNodes) {
    if ($node -is [System.Management.Automation.Language.IfStatementAst]) {
        $nesting = Get-NestingLevel $node
        $cognitiveComplexity += 1 + $nesting
        $elseIfCount = $node.Clauses.Count - 1
        if ($elseIfCount -gt 0) {
            $cognitiveComplexity += $elseIfCount
        }
    } elseif ($node -is [System.Management.Automation.Language.CatchClauseAst]) {
        $nesting = Get-NestingLevel $node
        $cognitiveComplexity += 1 + $nesting
    } else {
        $nesting = Get-NestingLevel $node
        $cognitiveComplexity += 1 + $nesting
    }
}

$binaryExpressions = $ast.FindAll({$args[0] -is [System.Management.Automation.Language.BinaryExpressionAst]}, $true)
foreach ($expr in $binaryExpressions) {
    if ($expr.Operator -in 'And', 'Or', 'Xor') {
        $parent = $expr.Parent
        if ($parent -is [System.Management.Automation.Language.BinaryExpressionAst] -and $parent.Operator -eq $expr.Operator) {
            # part of sequence
        } else {
            $cognitiveComplexity += 1
        }
    }
}

$xmlWriter.WriteAttributeString("cognitiveComplexity", $cognitiveComplexity);

Foreach ($item in $tokens) {	
	$xmlWriter.WriteStartElement("Token");
	$xmlWriter.WriteElementString("Text", $item.Text);
	$xmlWriter.WriteElementString("Value", $item.Value);
	$xmlWriter.WriteElementString("TokenFlags", $item.TokenFlags);
	$xmlWriter.WriteElementString("Kind", [System.Management.Automation.Language.TokenKind]::GetName([System.Management.Automation.Language.TokenKind], $item.Kind.value__));
	$xmlWriter.WriteElementString("StartLineNumber", $item.Extent.StartLineNumber);
	$xmlWriter.WriteElementString("CType", $item.GetType().Name);
	$xmlWriter.WriteElementString("EndLineNumber", $item.Extent.EndLineNumber);
	$xmlWriter.WriteElementString("StartOffset", $item.Extent.StartOffset);
	$xmlWriter.WriteElementString("EndOffset", $item.Extent.EndOffset);
	$xmlWriter.WriteElementString("StartColumnNumber", $item.Extent.StartColumnNumber);
	$xmlWriter.WriteElementString("EndColumnNumber", $item.Extent.EndColumnNumber);
	$xmlWriter.WriteEndElement(); 
}

$xmlWriter.WriteEndElement();
$xmlWriter.WriteEndDocument();
$xmlWriter.Finalize;
$xmlWriter.Flush;
$xmlWriter.Close();
