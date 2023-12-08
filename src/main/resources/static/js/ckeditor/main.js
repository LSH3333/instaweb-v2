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



// submit 
document.querySelector('#submit').addEventListener('click', () => {
	const editorData = editor.getData();
	console.log(editorData)

	pressSubmitBtn(editorData);
});

async function pressSubmitBtn(editorData) {
	await uploadToServer(editorData);
}

function uploadToServer(editorData) {
	return new Promise(function (resolve, reject) {
		const formData = new FormData();

		formData.append("content", editorData);

		const uploadToServerXhr = new XMLHttpRequest();
		uploadToServerXhr.open("POST", "/page/upload", true);
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