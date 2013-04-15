<?php include("htmltop.php") ?>

		<title>Fuelly</title> 
		
<?php include("header.php") ?>

	<h3></h3>
		<style type="text/css">
			span a:link {
				text-decoration: none
			}
			span a:visited {text-decoration: none}
		</style>
		<script type="text/javascript" src="fuelly/jquery.js"></script>
		<script type="text/javascript">
			jQuery.noConflict();
		</script>
		
		<?php
			//fuel , CC, Year & Make & Model, URL
			//{x:28, y:250, m:'Bike Make & Model Here', url: 'http://fuelly.com/motorcycle/honda/cbr250r', marker:{radius:4, states:{hover:{radius:10}}}},
		
			$datalist="";
			$chartseriesdata="";
			$lastmake="";
			$unit=trim($_GET["unit"]);
			if(!$unit)
				$unit=trim($_POST["unit"]);
			if(!$unit)
				$unit=1;
			$search="NC700";
			$newsearch=trim($_GET["search"]);
			if(!$newsearch)
				$newsearch=trim($_POST["search"]);
			if($newsearch)
				$search=$newsearch;
			$search = strtolower($search);
			$file = file_get_contents('fuelly.json');
			$decoded = json_decode($file);
			$json = $decoded->data;
 			$counter=0;
 			$last=count($json)-1;
 			$l=0;

			foreach ( $json as $row ){ // json sample access to nested $name = $comment->from->name; $message = $comment->message;
				$counter++;

				$m = explode(" ", $row->m );
				$make = $m[0];
				$model="";
				$modellen=sizeof($m);
				for($i=1; $i<$modellen; $i++){
					$model .= $m[$i];
					if($i<$modellen-1)
						$model .=" ";
				}
				$datalist .= "<option value=\"".$row->m."\"></option>\n "; //fullmake model
				if($make != $lastmake){
					$lastmake = $make;
					$datalist .= "<option value=\"".$make."\"></option>\n "; //make
				}
				$datalist .= "<option value=\"".$model."\"></option>\n "; //model

				$econ = $row->x * $unit;
				if($unit < 1)
					$econ = 1/$econ;
				$chartseriesdata .= "{x:".$econ.", y:".$row->y.", m:'".$row->m."', url:'".$row->url."', marker:{radius:".$row->marker->radius.", states:{hover:{radius:".$row->marker->states->hover->radius;
				$s = "/".$search."/";
				if(preg_match($s, strtolower($make)) || preg_match($s, strtolower($model)) || preg_match($s, strtolower($row->m))){
					$chartseriesdata .=  ", fillColor:'rgba(200, 0, 0, .65)'}}, fillColor:'rgba(200, 0, 0, .65)'}}";
				}
				else
					$chartseriesdata .= "}}}}";
				if(!($l++ === $last)) {
					$chartseriesdata .= ",\n";
				}
				
			}
		?>

		<!-- Chart Data -->
		<script type="text/javascript">
			(function($){ // encapsulate jQuery
				$(function () {
					var chart;
					$(document).ready(function() {
						chart = new Highcharts.Chart({
							chart: {
								renderTo: 'container',
								type: 'scatter',
								zoomType: 'xy',
								borderRadius: 10,
								borderWidth: 1,
								borderColor: '#ababab',
								spacingBottom: 10
							},
							colors: ['#3ABAB7'],
							title: {
								text: '',
							},
							subtitle: {
								text: ''
							},
							xAxis: {
								title: {
									enabled: true,
									text: 'Average Fuel Economy',
									style: {
										color: '#333333',
										fontWeight: 'bold'
									}
								},
								min: 0,
								startOnTick: false,
								endOnTick: false,
								showLastLabel: true
							},
							yAxis: {
								title: {
									text: 'Engine Size (cc)',
									style: {
										color: '#333333',
										fontWeight: 'bold'
									}
								},
								allowDecimals:false,
								min: 50,
								max: 1800,
							 	minorGridLineWidth: 1,
							 	gridLineWidth: 1,
							 	alternateGridColor: '#efefef',
							},
							tooltip: {
								formatter: function() {
										return '' + 
										this.point.m + '<br>'+
										this.x +' km/l, '+ //update from php value
										 this.y +' cc'; 
								}
							},
							legend: {
								layout: 'vertical',
								align: 'left',
								verticalAlign: 'top',
								x: 100,
								y: 70,
								floating: true,
								backgroundColor: '#FFFFFF',
								borderWidth: 1
							},
							plotOptions: {
								scatter: {
									point: {
										events: {
											click: function() {
												window.open(this.options.url); //opens in new window or tab
												//location.href = this.options.url; opens in same window
											}
										}
									}
								}
							},
							legend: {enabled:false},
							credits: {enabled:false},
							series: [{
								name: 'All', //will be 'Sport' once models are assigned to categorie
								color: 'rgba(58, 186, 183, .65)',
								data: [
								
									<?php echo $chartseriesdata; ?>

									]
							}, 
							//other sample motorcycle type to use with legend filtering
							{
								name: 'Other', //will be 'Sport' once models are assigned to categories
								color: 'rgba(58, 186, 183, .65)',
								data: [
									//fuel , CC, Year & Make & Model, URL
									//{x:28, y:250, m:'Bike Make & Model Here', url: 'http://fuelly.com/motorcycle/honda/cbr250r', marker:{radius:4, states:{hover:{radius:10}}}},

								]
							}
							]
						});
					});
					
				});
			})(jQuery);
		</script>
		<?php
		/* sample search form i used on another project */
		echo "<table>";
		echo "	<tr><td valign='top'>";
		echo "		<form action=\"fuelly.php\" method=\"get\">\n";
		echo "			<input type='hidden' name='unit' value=".$unit.">\n";
		echo "			<input type=\"text\" size=\"80\" name=\"search\"";
		if($search)
			echo " value=\"".$search."\""; 
		echo " placeholder=\"search\" list=\"list\">\n";
		echo "			<datalist id=\"list\">\n";

		echo $datalist;

		echo "			</datalist>\n";
		echo "		<td valign='top'>";
		echo "			<input type=\"Submit\" value=\"Search\"> </form>\n";
		echo "		<td valign='top'>";
		echo "			<form method=\"link\" action=\"fuelly.php\">\n";
		echo "			<input type=\"submit\" value=\"Reset\"> </form>\n";
		echo "</table>\n";
		?>

		<div id="content">
		     
			<p>Currently showing <?php echo $counter; ?> of a possible  <b>1,000</b> data points (per data series).<br></p>
			<script src="fuelly/highcharts.js"></script>
			<div id="container" style="min-width: 400px; height: 400px; margin: 0 auto">
			</div>
			<script type="text/javascript">
				Highcharts.theme = { colors: ['#4572A7'] };// prevent errors in default theme
				var highchartsOptions = Highcharts.getOptions(); 
			</script>	
		</div><!-- e: content -->
		<center>
			<span>
		<?php
			if($unit!=1) echo "		<a class='unit' href='fuelly.php?unit=1&search=".$search."'>KM/L</a>\n";
			else echo " KM/L \n";
			if($unit!=2.35215) echo "		<a class='unit' href='fuelly.php?unit=2.35215&search=".$search."'>MPG</a>\n";
			else echo " MPG \n";
			if($unit!=2.824809363) echo "		<a class='unit' href='fuelly.php?unit=2.824809363&search=".$search."'>UK</a>\n";
			else echo " UK \n";
			if($unit!=0.01) echo "		<a class='unit' href='fuelly.php?unit=0.01&search=".$search."'>L/100KM</a>\n";
			else echo " L/100KM \n";
		?>
			</span>
		</center>
		<br>
		<p>Sample motorcycle data from fuelly.com had quite a few data annomalies meaning that an algorithm was needed to clean up up the data and remove outliers. Once upper and lower bounds where established, interquartile ranges where used to express the average "mileage" for a motorcycle and plotted accordingly. For more information on a specific subset of the data use your mouse to create a zoom box, or click on a specific model to see all of it's data sources.</p>
<p>The visualization shows a clear pattern, with populare motorcycles represented by more sample data (larger circles), and ultra fuel efficient bikes like the Honda NC700 really standing out from the crowd.</p>
<?php include("footer.php") ?>