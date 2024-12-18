import {StyleSheet, Dimensions} from 'react-native';

const {width, height} = Dimensions.get('window');
const responsiveFontSize = (factor: number) => width * (factor / 100);
const responsiveWidth = (factor: number) => width * (factor / 100);
const responsiveHeight = (factor: number) => height * (factor / 100);

const styles = StyleSheet.create({
  buttonContainerWithMargin: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    marginBottom: responsiveHeight(1),
    marginRight: responsiveWidth(1.5),
  },
  buttonWrapper: {
    flex: 6,
    marginBottom: responsiveHeight(4),
    height: responsiveHeight(6),
    // justifyContent: 'center', // 세로 가운데 정렬
    // alignItems: 'center',     // 가로 가운데 정렬
  },
  disabledButton: {
    backgroundColor: '#D3D3D3', // 비활성화된 버튼 색상
  },
  iconWrapper: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  iconImage: {
    width: responsiveWidth(15),
    height: responsiveWidth(15),
    marginBottom: responsiveFontSize(2),
  },
  disabledIcon: {
    tintColor: '#A9A9A9', // 회색으로 변경하여 비활성화 상태 표시
    opacity: 0.1, // 불투명도 낮춰 더 흐릿하게
  },
});

export default styles;
