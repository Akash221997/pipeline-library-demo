def call (var1,var2)
{

REGION="eu-west-2"


type jq >/dev/null 2>&1 || { echo >&2 "The jq utility is required for this script to run."; exit 1; }


type aws >/dev/null 2>&1 || { echo >&2 "The aws cli is required for this script to run."; exit 1; }


if [ $# -ne 2 ]; then
	        echo "Useage ./clean-old-ecr-images.sh <IMAGE-REPO-NAME> <WEEKS-TO-KEEP>"
		        exit 1
fi

REPO=${var1}
WEEKS=${var2}
SECONDS=$(echo "$WEEKS * 604800" | bc)

case "$CHOICE" in
	  y|Y)
		      WEEKS_AGO=$(echo "$(date +%s)-$SECONDS" | bc)
		          IMAGES=$(aws --region $REGION ecr describe-images --repository-name $REPO --output json | jq '.[]' | jq '.[]' | jq "select (.imagePushedAt < $WEEKS_AGO)" | jq -r '.imageDigest')
			      for IMAGE in ${IMAGES[*]}; do
				            echo "Deleting $IMAGE"
					          aws --region $REGION ecr batch-delete-image --repository-name $REPO --image-ids imageDigest=$IMAGE
						      done
						        ;;

							  *) exit 0  ;;
						  esac
						  echo "Finished."
}					  
