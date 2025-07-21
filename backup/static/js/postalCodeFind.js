// 다음 우편번호 API 호출 함수
function sample6_execDaumPostcode() {
    new daum.Postcode({
        oncomplete: function(data) {
            document.getElementById('sample6_postcode').value = data.zonecode;
            document.getElementById('sample6_address').value = data.address;
            document.getElementById('sample6_extraAddress').value = data.buildingName || '';
        }
    }).open();
}

document.addEventListener('DOMContentLoaded', function() {
    const modal = document.getElementById('addressModal');
    const openBtn = document.getElementById('openModalBtn');
    const closeBtn = document.getElementById('closeModalBtn');

    openBtn.addEventListener('click', () => {
        modal.classList.remove('hidden');
    });

    closeBtn.addEventListener('click', () => {
        modal.classList.add('hidden');
    });

    window.addEventListener('click', (event) => {
        if (event.target === modal) {
            modal.classList.add('hidden');
        }
    });

    document.getElementById('registerAddressBtn').addEventListener('click', () => {
        const postcode = document.getElementById('sample6_postcode').value.trim();
        const address = document.getElementById('sample6_address').value.trim();
        const detailAddress = document.getElementById('sample6_detailAddress').value.trim();
        const extraAddress = document.getElementById('sample6_extraAddress').value.trim();

        if (!postcode || !address || !detailAddress || !extraAddress) {
            alert('모든 주소 정보를 입력해주세요.');
            return;
        }

        const addressData = { postcode, address, detailAddress, extraAddress };

        fetch('/api/address', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(addressData)
        })
            .then(response => {
                if (!response.ok) {
                    return response.text().then(err => {
                        throw new Error(err || '서버 오류');
                    });
                }
                return response.text();
            })
            .then(result => {
                alert('주소가 성공적으로 등록되었습니다!');
                modal.classList.add('hidden');
                closeModal();
            })
            .catch(error => {
                alert('주소 등록 실패: ' + error.message);
                console.error('에러:', error);
            });
    });
});
