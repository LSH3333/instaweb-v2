

// ckeditor5 클래식 에디터 
ClassicEditor
	.create(document.querySelector('.editor'), {
		// Editor configuration. (플러그인 등 설정 여기서)
	})
	.then(editor => {
		window.editor = editor; // ckeditor5 editor
	})
	.catch(handleSampleError);

// 에러 
function handleSampleError(error) {
	const issueUrl = 'https://github.com/ckeditor/ckeditor5/issues';

	const message = [
		'Oops, something went wrong!',
		`Please, report the following error on ${issueUrl} with the build id "24wli16rgyf0-unt8fr6ckh47" and the error stack trace:`
	].join('\n');

	console.error(message);
	console.error(error);
}

// submit 버튼 
const submintBtn = document.getElementById('submit');
const submitAnchor = document.getElementById('submitAnchor')

submintBtn.addEventListener('click', function (event) {
	event.preventDefault();
	const editorData = editor.getData();
	pressSubmitBtn(editorData);
});

// submit 버튼 클릭, 작성 내용 서버에 업로드 완료 될때까지 기다리고 홈으로 이동 
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
		if (frontImg === null) {
			formData.append("frontImg", "NULL")
		} else {
			formData.append("frontImg", frontImg.src)
		}
		// frontText
		const frontText = getFrontText(editorData);
		formData.append("frontText", frontText.substring(0, 100));
		// title
		let title = document.getElementById('input-title').value
		formData.append("title", title);

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

// 미리보기 카드용 이미지
// 작성된 내용중에 <img> 1개 찾아서 리턴 
function getFrontImg(editorData) {
	const tempDiv = document.createElement("div");
	tempDiv.innerHTML = editorData;
	const imgElement = tempDiv.querySelector('img');
	return imgElement;
}

function getFrontText() {
	return document.getElementById('input-title').value;
}


// // 미리보기 카드용 텍스트 
// // 재귀적으로 자식으로 들어가면서 TEXT_NODE 에서 text 만 쁩는다 
// function getFrontText(editorData) {
// 	const tempDiv = document.createElement("div");
// 	tempDiv.innerHTML = editorData;

// 	// element 들 순회하면서 text 만 뽑는다 
// 	function extractText(element) {
// 		let text = '';
// 		for (let childNode of element.childNodes) {
// 			if (childNode.nodeType === Node.TEXT_NODE) {
// 				text += childNode.nodeValue;
// 			} else if (childNode.nodeType === Node.ELEMENT_NODE) {
// 				// TEXT_NODE 아니면 재귀적으로 자식으로 계속 파고 들어가면서 찾는다 
// 				text += extractText(childNode);
// 			}
// 		}
// 		return text;
// 	}

// 	// 재귀 시작 
// 	const textContent = extractText(tempDiv);

// 	return textContent;
// }

