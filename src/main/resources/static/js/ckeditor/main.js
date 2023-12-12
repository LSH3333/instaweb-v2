ClassicEditor
	.create(document.querySelector('.editor'), {
		// Editor configuration.
	})
	.then(editor => {
		window.editor = editor;
	})
	.catch(handleSampleError);

function handleSampleError(error) {
	const issueUrl = 'https://github.com/ckeditor/ckeditor5/issues';

	const message = [
		'Oops, something went wrong!',
		`Please, report the following error on ${issueUrl} with the build id "24wli16rgyf0-unt8fr6ckh47" and the error stack trace:`
	].join('\n');

	console.error(message);
	console.error(error);
}


const submintBtn = document.getElementById('submit');
const submitAnchor = document.getElementById('submitAnchor')

submintBtn.addEventListener('click', function(event) {
	event.preventDefault();
	const editorData = editor.getData();
	pressSubmitBtn(editorData);	
	
});

async function pressSubmitBtn(editorData) {
	await uploadToServer(editorData);
	submitAnchor.click();
}

function uploadToServer(editorData) {
	return new Promise(function (resolve, reject) {
		const formData = new FormData();

		// content
		formData.append("content", editorData);

		// frontImg
		const frontImg = getFrontImg(editorData)
		if(frontImg === null) {
			formData.append("frontImg", "NULL")
		} else {
			formData.append("frontImg", frontImg.src)
		}
		// frontText
		const frontText = getFrontText(editorData);
		formData.append("frontText", frontText.substring(0, 100));

		const uploadToServerXhr = new XMLHttpRequest();
		uploadToServerXhr.open("POST", "/pages/upload", true);
		uploadToServerXhr.onload = function () {
			if (uploadToServerXhr.status == 200) {
				console.log("Files uploaded successfully");
				resolve();
			} else {
				reject(new Error("Error upload files"));
			}
		};

		uploadToServerXhr.onerror = function () {
			reject(new Error("Network error"));
		};

		uploadToServerXhr.send(formData);
	})
}

function getFrontImg(editorData) {
	const tempDiv = document.createElement("div");
	tempDiv.innerHTML = editorData;
	const imgElement = tempDiv.querySelector('img');
	return imgElement;
}

function getFrontText(editorData) {
    const tempDiv = document.createElement("div");
    tempDiv.innerHTML = editorData;

    // Recursive function to extract text content from nested elements
    function extractText(element) {
        let text = '';
        for (let childNode of element.childNodes) {
            if (childNode.nodeType === Node.TEXT_NODE) {
                // If it's a text node, append the text content
                text += childNode.nodeValue;
            } else if (childNode.nodeType === Node.ELEMENT_NODE) {
                // If it's an element node, recursively extract text from its children
                text += extractText(childNode);
            }
        }
        return text;
    }

    // Call the recursive function on the main div
    const textContent = extractText(tempDiv);

    // Use the extracted text content as needed
    console.log(textContent);
	return textContent;
}