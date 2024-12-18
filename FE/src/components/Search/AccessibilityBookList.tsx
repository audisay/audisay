import React, { useState, useEffect } from 'react';
import { View, Text, StyleSheet, TouchableOpacity, Image, ScrollView, Dimensions } from 'react-native';
import { useNavigation } from '@react-navigation/native';
import { RootStackParamList } from '../../navigation/AppNavigator';
import { StackNavigationProp } from '@react-navigation/stack';

const { width, height } = Dimensions.get('window');

interface Book {
  bookId: number;
  title: string;
  author: string;
  publishedAt: string;
  cover: string; // 변경: URL로 올 경우 string 타입으로 설정
}

interface AccessibilityBookListProps {
  bookList: Book[];
  updateSortAndFetch: (sortBy: 'published_date' | 'title' | null, sortOrder: 'asc' | 'desc') => void
  onResetStates?: (resetFunction: () => void) => void;
}

type NavigationProp = StackNavigationProp<RootStackParamList, 'BookDetail'>;

const AccessibilityBookList: React.FC<AccessibilityBookListProps> = ({ bookList, updateSortAndFetch, onResetStates }) => {
  const [sortOrder, setSortOrder] = useState<'correct' | 'published_date' | 'title'>('correct');
  const [isLastest, setIsLastest] = useState<boolean>(true);
  const [isAlpabetAsc, setIsAlpabetAsc] = useState<boolean>(true);
  // const [isAscending, setIsAscending] = useState(true);
  const navigation = useNavigation<NavigationProp>();

  const resetStates = () => {
    setSortOrder('correct');
    setIsLastest(true);
    setIsAlpabetAsc(true);
  };

  useEffect(() => {
    if (onResetStates) {
      onResetStates(resetStates); // 초기화 함수 전달
    }
  }, [onResetStates]);

  const handleSortChange = (order: 'correct' | 'published_date' | 'title') => {
    if(order === 'published_date'){
      if(sortOrder === order) {
        setIsLastest((prev) => {
          const nextLastest = !prev;
          updateSortAndFetch('published_date', nextLastest ? 'desc' : 'asc');
          return nextLastest;
        });
      } else {
        updateSortAndFetch('published_date', isLastest ? 'desc' : 'asc');
      }
    }else if(order === 'title'){
      if(sortOrder === order) {
        setIsAlpabetAsc((prev) => {
          const nextAlpabetAsc = !prev;
          updateSortAndFetch('title', nextAlpabetAsc ? 'asc' : 'desc');
          return nextAlpabetAsc;
        });
      } else {
        updateSortAndFetch('title', isAlpabetAsc ? 'asc' : 'desc');
      }
    } else {
      updateSortAndFetch(null, 'desc'); // 정확도순은 기본값
    }
    setSortOrder(order);
  };

  // const toggleSortDirection = () => {
  //   setIsAscending((prev) => !prev);
  // };

  const handleBookClick = (bookId: number) => {
    navigation.navigate('BookDetail', { bookId });
  };

  // const sortedBooks = [...bookList].sort((a, b) => {
  //   if (sortOrder === 'latest') {
  //     return isAscending
  //       ? new Date(a.publishedAt).getTime() - new Date(b.publishedAt).getTime()
  //       : new Date(b.publishedAt).getTime() - new Date(a.publishedAt).getTime();
  //   } else {
  //     return isAscending
  //       ? a.title.localeCompare(b.title)
  //       : b.title.localeCompare(a.title);
  //   }
  // });

  return (
    <View style={styles.container}>
      <View style={styles.sortContainer}>
        <TouchableOpacity
            style={[styles.sortButton, sortOrder === 'correct' && styles.selectedButton]}
            onPress={() => handleSortChange('correct')}
            accessibilityLabel={'정확도 기준 정렬 버튼.'}
            accessibilityHint={'정확도 순으로 정렬 합니다.'}
        >
          <Text style={styles.sortButtonText}
          >정확도순</Text>
        </TouchableOpacity>
        <TouchableOpacity
          style={[styles.sortButton, sortOrder === 'published_date' && styles.selectedButton]}
          onPress={() => handleSortChange('published_date')}
          accessibilityLabel={`출판일 기준 정렬 버튼. 현재 ${isLastest ? '최신순' : '오래된순'}.`}
          accessibilityHint={`활성화 시 ${sortOrder === 'published_date' ?
              !isLastest ? '최신순' : '오래된순' :
              isLastest ? '최신순' : '오래된순'}으로 정렬 합니다.`}
        >
          <Text style={styles.sortButtonText}>{isLastest ? '최신순' : '오래된순'}</Text>
        </TouchableOpacity>
        <TouchableOpacity
          style={[styles.sortButton, sortOrder === 'title' && styles.selectedButton]}
          onPress={() => handleSortChange('title')}
          accessibilityLabel={`제목순 기준 정렬 버튼. 현재 ${isAlpabetAsc ? '오름차순' : '내림차순'}.`}
          accessibilityHint={`활성화 시 ${sortOrder === 'title' ?
              !isAlpabetAsc ? '오름차순' : '내림차순' :
              isAlpabetAsc ? '오름차순' : '내림차순'}으로 정렬 합니다.`}
        >
          <Text style={styles.sortButtonText}>{isAlpabetAsc ? '제목순(ㄱ)' : '제목순(ㅎ)'}</Text>
        </TouchableOpacity>
        {/*<TouchableOpacity style={styles.reverseButton} onPress={toggleSortDirection}>*/}
        {/*  <Image source={require('../../assets/icons/reverse.png')} style={styles.reverseIcon} />*/}
        {/*</TouchableOpacity>*/}
      </View>
      <ScrollView>
        {bookList.map((book) => (
          <TouchableOpacity key={book.bookId} onPress={() => handleBookClick(book.bookId)} style={styles.bookItem}>
            <Image source={{ uri: book.cover }} style={styles.bookImage} />
            <View style={styles.bookInfo}>
              <Text style={styles.bookTitle} numberOfLines={2}>{book.title}</Text>
              <Text style={[styles.bookAuthor, styles.textSpacing]} numberOfLines={1}>저자: {book.author}</Text>
              <Text style={[styles.bookPublishedAt, styles.textSpacing]}>출판일: {book.publishedAt}</Text>
            </View>
          </TouchableOpacity>
        ))}
      </ScrollView>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: width * 0.01,
    backgroundColor: '#FFFFFF',
  },
  sortContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: height * 0.01,
  },
  sortButton: {
    flex: 1,
    paddingVertical: height * 0.015,
    backgroundColor: '#E0E0E0',
    borderRadius: width * 0.03,
    alignItems: 'center',
    marginHorizontal: width * 0.005,
  },
  selectedButton: {
    backgroundColor: '#3943B7',
  },
  sortButtonText: {
    color: 'white',
    fontWeight: 'bold',
    fontSize: width * 0.05,
  },
  reverseButton: {
    padding: height * 0.012,
    backgroundColor: '#FF6347',
    borderRadius: width * 0.03,
  },
  reverseIcon: {
    width: width * 0.08,
    height: width * 0.08,
  },
  bookItem: {
    flexDirection: 'row',
    marginBottom: height * 0.03,
    borderBottomWidth: 1,
    borderBottomColor: '#CCCCCC',
    paddingBottom: height * 0.02,
  },
  bookImage: {
    width: width * 0.3,
    height: height * 0.25,
    marginRight: width * 0.05,
  },
  bookInfo: {
    flex: 1,
    justifyContent: 'center',
  },
  bookTitle: {
    fontSize: width * 0.09,
    fontWeight: 'bold',
    marginBottom: height * 0.02,
  },
  bookAuthor: {
    fontSize: width * 0.06,
  },
  bookPublishedAt: {
    fontSize: width * 0.06,
  },
  textSpacing: {
    marginBottom: height * 0.025,
  },
});

export default AccessibilityBookList;
