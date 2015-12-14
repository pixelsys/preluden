

echo -e "Make dir dialogues in OC"
mkdir	-p ../openclinica/dialogues

echo -e "Copy dialogues/* into OC"
cp dialogues/*	../openclinica/dialogues


 mkdir -p ../openclinica/includes
 
echo -e "Copy js/OCWidgets.js into openclinica/includes"
cp js/OCWidgets.js 	../openclinica/includes

echo -e "Copy js/prototype.js into openclinica/includes"
cp js/prototype.js	../openclinica/includes

echo -e "Copy js/jquery-ui.js into openclinica/includes"
cp js/jquery-ui.js	../openclinica/includes

echo -e "Copy css/OCWidgets-style.css into openclinica/includes"
cp css/OCWidgets-style.css	../openclinica/includes


echo -e "Copy css/jquery-ui.css into openclinica/includes"
cp css/jquery-ui.css	../openclinica/includes


echo -e "Copy css/OCSpinner.gif into openclinica/includes"
cp css/OCSpinner.gif	../openclinica/includes



mkdir -p ../openclinica/includes/images
 
echo -e "Copy images/ui-icons_888888_256x240.png into openclinica/includes/images"
cp images/ui-icons_888888_256x240.png	../openclinica/includes/images

echo -e "Copy images/ui-icons_454545_256x240.png into openclinica/includes/images"
cp images/ui-icons_454545_256x240.png	../openclinica/includes/images   



