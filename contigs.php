<?php
$servername = "localhost";
$username = "hack";
$password = "HackCVR16";
$dbname = "Hack";

//$id=$argv[1];
$id ="midge1-0167e2:idba.genome1_contig-121_1027";
echo "ContigID: " . $id . "\n";

$outputFile = " ". $id . ".fasta";

echo "FileName: " . $outputFile;


// Create connection

$conn = mysqli_connect($servername, $username, $password, $dbname);
// Check connection
if (!$conn) {
    die("Connection failed: " . mysqli_connect_error());
}

$sql = "SELECT ContigID , Seq  FROM MergeTable WHERE ContigID = '$id'";
$result = mysqli_query($conn, $sql);

if (mysqli_num_rows($result) > 0) {
    // output data of each row
    while($row = mysqli_fetch_assoc($result)) {
        echo ">" . $row["ContigID"]. "\n" . $row["Seq"].  "\n";
       // Open the file to get existing content
      // $current = file_get_contents($outputFile);
       // Append a new person to the file
       //$current .= ">" . $row["ContigID"]. "\n" . $row["Seq"].  "\n";
       $current = ">" . $row["ContigID"]. "\n" . $row["Seq"].  "\n";
       // Write the contents back to the file
       file_put_contents($outputFile, $current);
       
    }
} else {
    echo "0 results\n";
}

mysqli_close($conn);

 


?>
