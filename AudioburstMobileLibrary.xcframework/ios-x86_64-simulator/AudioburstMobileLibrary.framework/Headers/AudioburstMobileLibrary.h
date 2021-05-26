#import <Foundation/NSArray.h>
#import <Foundation/NSDictionary.h>
#import <Foundation/NSError.h>
#import <Foundation/NSObject.h>
#import <Foundation/NSSet.h>
#import <Foundation/NSString.h>
#import <Foundation/NSValue.h>

@class AMLListenedBurstModelAdapter, AMLBurst, AMLLibraryError, AMLPendingPlaylist, AMLPlaylistInfo, AMLPlaylist, NSData, AMLUserPreferences, AMLPlaybackState, AMLDuration, AMLBurstSource, AMLCtaData, AMLDateTime, AMLDurationUnit, AMLKotlinEnum<E>, AMLKotlinArray<T>, AMLKey, AMLPlayerActionType, AMLPlayerAction, AMLPreference, AMLResult<__covariant T>, AMLKotlinNothing, AMLListenedBurstModel, AMLRuntimeQuery<__covariant RowType>, AMLRuntimeTransacterTransaction, AMLKotlinByteArray, AMLKotlinByteIterator;

@protocol AMLListenedBurstModelQueries, AMLRuntimeTransactionWithoutReturn, AMLRuntimeTransactionWithReturn, AMLRuntimeTransacter, AMLDatabase, AMLRuntimeSqlDriver, AMLRuntimeSqlDriverSchema, AMLPlaybackStateListener, AMLKotlinComparable, AMLRuntimeColumnAdapter, AMLRuntimeTransactionCallbacks, AMLRuntimeSqlPreparedStatement, AMLRuntimeSqlCursor, AMLRuntimeCloseable, AMLKotlinIterator, AMLRuntimeQueryListener;

NS_ASSUME_NONNULL_BEGIN
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Wunknown-warning-option"
#pragma clang diagnostic ignored "-Wincompatible-property-type"
#pragma clang diagnostic ignored "-Wnullability"

__attribute__((swift_name("KotlinBase")))
@interface AMLBase : NSObject
- (instancetype)init __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
+ (void)initialize __attribute__((objc_requires_super));
@end;

@interface AMLBase (AMLBaseCopying) <NSCopying>
@end;

__attribute__((swift_name("KotlinMutableSet")))
@interface AMLMutableSet<ObjectType> : NSMutableSet<ObjectType>
@end;

__attribute__((swift_name("KotlinMutableDictionary")))
@interface AMLMutableDictionary<KeyType, ObjectType> : NSMutableDictionary<KeyType, ObjectType>
@end;

@interface NSError (NSErrorAMLKotlinException)
@property (readonly) id _Nullable kotlinException;
@end;

__attribute__((swift_name("KotlinNumber")))
@interface AMLNumber : NSNumber
- (instancetype)initWithChar:(char)value __attribute__((unavailable));
- (instancetype)initWithUnsignedChar:(unsigned char)value __attribute__((unavailable));
- (instancetype)initWithShort:(short)value __attribute__((unavailable));
- (instancetype)initWithUnsignedShort:(unsigned short)value __attribute__((unavailable));
- (instancetype)initWithInt:(int)value __attribute__((unavailable));
- (instancetype)initWithUnsignedInt:(unsigned int)value __attribute__((unavailable));
- (instancetype)initWithLong:(long)value __attribute__((unavailable));
- (instancetype)initWithUnsignedLong:(unsigned long)value __attribute__((unavailable));
- (instancetype)initWithLongLong:(long long)value __attribute__((unavailable));
- (instancetype)initWithUnsignedLongLong:(unsigned long long)value __attribute__((unavailable));
- (instancetype)initWithFloat:(float)value __attribute__((unavailable));
- (instancetype)initWithDouble:(double)value __attribute__((unavailable));
- (instancetype)initWithBool:(BOOL)value __attribute__((unavailable));
- (instancetype)initWithInteger:(NSInteger)value __attribute__((unavailable));
- (instancetype)initWithUnsignedInteger:(NSUInteger)value __attribute__((unavailable));
+ (instancetype)numberWithChar:(char)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedChar:(unsigned char)value __attribute__((unavailable));
+ (instancetype)numberWithShort:(short)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedShort:(unsigned short)value __attribute__((unavailable));
+ (instancetype)numberWithInt:(int)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedInt:(unsigned int)value __attribute__((unavailable));
+ (instancetype)numberWithLong:(long)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedLong:(unsigned long)value __attribute__((unavailable));
+ (instancetype)numberWithLongLong:(long long)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedLongLong:(unsigned long long)value __attribute__((unavailable));
+ (instancetype)numberWithFloat:(float)value __attribute__((unavailable));
+ (instancetype)numberWithDouble:(double)value __attribute__((unavailable));
+ (instancetype)numberWithBool:(BOOL)value __attribute__((unavailable));
+ (instancetype)numberWithInteger:(NSInteger)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedInteger:(NSUInteger)value __attribute__((unavailable));
@end;

__attribute__((swift_name("KotlinByte")))
@interface AMLByte : AMLNumber
- (instancetype)initWithChar:(char)value;
+ (instancetype)numberWithChar:(char)value;
@end;

__attribute__((swift_name("KotlinUByte")))
@interface AMLUByte : AMLNumber
- (instancetype)initWithUnsignedChar:(unsigned char)value;
+ (instancetype)numberWithUnsignedChar:(unsigned char)value;
@end;

__attribute__((swift_name("KotlinShort")))
@interface AMLShort : AMLNumber
- (instancetype)initWithShort:(short)value;
+ (instancetype)numberWithShort:(short)value;
@end;

__attribute__((swift_name("KotlinUShort")))
@interface AMLUShort : AMLNumber
- (instancetype)initWithUnsignedShort:(unsigned short)value;
+ (instancetype)numberWithUnsignedShort:(unsigned short)value;
@end;

__attribute__((swift_name("KotlinInt")))
@interface AMLInt : AMLNumber
- (instancetype)initWithInt:(int)value;
+ (instancetype)numberWithInt:(int)value;
@end;

__attribute__((swift_name("KotlinUInt")))
@interface AMLUInt : AMLNumber
- (instancetype)initWithUnsignedInt:(unsigned int)value;
+ (instancetype)numberWithUnsignedInt:(unsigned int)value;
@end;

__attribute__((swift_name("KotlinLong")))
@interface AMLLong : AMLNumber
- (instancetype)initWithLongLong:(long long)value;
+ (instancetype)numberWithLongLong:(long long)value;
@end;

__attribute__((swift_name("KotlinULong")))
@interface AMLULong : AMLNumber
- (instancetype)initWithUnsignedLongLong:(unsigned long long)value;
+ (instancetype)numberWithUnsignedLongLong:(unsigned long long)value;
@end;

__attribute__((swift_name("KotlinFloat")))
@interface AMLFloat : AMLNumber
- (instancetype)initWithFloat:(float)value;
+ (instancetype)numberWithFloat:(float)value;
@end;

__attribute__((swift_name("KotlinDouble")))
@interface AMLDouble : AMLNumber
- (instancetype)initWithDouble:(double)value;
+ (instancetype)numberWithDouble:(double)value;
@end;

__attribute__((swift_name("KotlinBoolean")))
@interface AMLBoolean : AMLNumber
- (instancetype)initWithBool:(BOOL)value;
+ (instancetype)numberWithBool:(BOOL)value;
@end;

__attribute__((swift_name("RuntimeTransacter")))
@protocol AMLRuntimeTransacter
@required
- (void)transactionNoEnclosing:(BOOL)noEnclosing body:(void (^)(id<AMLRuntimeTransactionWithoutReturn>))body __attribute__((swift_name("transaction(noEnclosing:body:)")));
- (id _Nullable)transactionWithResultNoEnclosing:(BOOL)noEnclosing bodyWithReturn:(id _Nullable (^)(id<AMLRuntimeTransactionWithReturn>))bodyWithReturn __attribute__((swift_name("transactionWithResult(noEnclosing:bodyWithReturn:)")));
@end;

__attribute__((swift_name("Database")))
@protocol AMLDatabase <AMLRuntimeTransacter>
@required
@property (readonly) id<AMLListenedBurstModelQueries> listenedBurstModelQueries __attribute__((swift_name("listenedBurstModelQueries")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("DatabaseCompanion")))
@interface AMLDatabaseCompanion : AMLBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<AMLDatabase>)invokeDriver:(id<AMLRuntimeSqlDriver>)driver listenedBurstModelAdapter:(AMLListenedBurstModelAdapter *)listenedBurstModelAdapter __attribute__((swift_name("invoke(driver:listenedBurstModelAdapter:)")));
@property (readonly) id<AMLRuntimeSqlDriverSchema> Schema __attribute__((swift_name("Schema")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("AudioburstLibrary")))
@interface AMLAudioburstLibrary : AMLBase
- (instancetype)initWithApplicationKey:(NSString *)applicationKey __attribute__((swift_name("init(applicationKey:)"))) __attribute__((objc_designated_initializer));
- (void)ctaButtonClickBurstId:(NSString *)burstId __attribute__((swift_name("ctaButtonClick(burstId:)")));
- (void)filterListenedBurstsEnabled:(BOOL)enabled __attribute__((swift_name("filterListenedBursts(enabled:)")));
- (void)getAdUrlBurst:(AMLBurst *)burst onData:(void (^)(NSString *))onData onError:(void (^)(AMLLibraryError *))onError __attribute__((swift_name("getAdUrl(burst:onData:onError:)")));
- (void)getPersonalPlaylistOnData:(void (^)(AMLPendingPlaylist *))onData onError:(void (^)(AMLLibraryError *))onError __attribute__((swift_name("getPersonalPlaylist(onData:onError:)")));
- (void)getPlaylistPlaylistInfo:(AMLPlaylistInfo *)playlistInfo onData:(void (^)(AMLPlaylist *))onData onError:(void (^)(AMLLibraryError *))onError __attribute__((swift_name("getPlaylist(playlistInfo:onData:onError:)")));
- (void)getPlaylistData:(NSData *)data onData:(void (^)(AMLPlaylist *))onData onError:(void (^)(AMLLibraryError *))onError __attribute__((swift_name("getPlaylist(data:onData:onError:)")));
- (void)getPlaylistsOnData:(void (^)(NSArray<AMLPlaylistInfo *> *))onData onError:(void (^)(AMLLibraryError *))onError __attribute__((swift_name("getPlaylists(onData:onError:)")));
- (void)getUserPreferencesOnData:(void (^)(AMLUserPreferences *))onData onError:(void (^)(AMLLibraryError *))onError __attribute__((swift_name("getUserPreferences(onData:onError:)")));
- (void)removePlaybackStateListenerListener:(id<AMLPlaybackStateListener>)listener __attribute__((swift_name("removePlaybackStateListener(listener:)")));
- (void)searchQuery:(NSString *)query onData:(void (^)(AMLPlaylist *))onData onError:(void (^)(AMLLibraryError *))onError __attribute__((swift_name("search(query:onData:onError:)")));
- (void)setAudioburstUserIDUserId:(NSString *)userId onData:(void (^)(AMLBoolean *))onData onError:(void (^)(AMLLibraryError *))onError __attribute__((swift_name("setAudioburstUserID(userId:onData:onError:)")));
- (void)setPlaybackStateListenerListener:(id<AMLPlaybackStateListener>)listener __attribute__((swift_name("setPlaybackStateListener(listener:)")));
- (void)setUserPreferencesUserPreferences:(AMLUserPreferences *)userPreferences onData:(void (^)(AMLUserPreferences *))onData onError:(void (^)(AMLLibraryError *))onError __attribute__((swift_name("setUserPreferences(userPreferences:onData:onError:)")));
- (void)start __attribute__((swift_name("start()")));
- (void)stop __attribute__((swift_name("stop()")));
@end;

__attribute__((swift_name("PlaybackStateListener")))
@protocol AMLPlaybackStateListener
@required
- (AMLPlaybackState * _Nullable)getPlaybackState __attribute__((swift_name("getPlaybackState()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Burst")))
@interface AMLBurst : AMLBase
- (instancetype)initWithId:(NSString *)id title:(NSString *)title creationDate:(NSString *)creationDate duration:(AMLDuration *)duration sourceName:(NSString *)sourceName category:(NSString * _Nullable)category playlistId:(int64_t)playlistId showName:(NSString *)showName streamUrl:(NSString * _Nullable)streamUrl audioUrl:(NSString *)audioUrl imageUrls:(NSArray<NSString *> *)imageUrls source:(AMLBurstSource *)source shareUrl:(NSString *)shareUrl keywords:(NSArray<NSString *> *)keywords ctaData:(AMLCtaData * _Nullable)ctaData adUrl:(NSString * _Nullable)adUrl __attribute__((swift_name("init(id:title:creationDate:duration:sourceName:category:playlistId:showName:streamUrl:audioUrl:imageUrls:source:shareUrl:keywords:ctaData:adUrl:)"))) __attribute__((objc_designated_initializer));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *audioUrl __attribute__((swift_name("audioUrl")));
@property (readonly) NSString * _Nullable category __attribute__((swift_name("category")));
@property (readonly) NSString *creationDate __attribute__((swift_name("creationDate")));
@property (readonly) AMLCtaData * _Nullable ctaData __attribute__((swift_name("ctaData")));
@property (readonly) AMLDuration *duration __attribute__((swift_name("duration")));
@property (readonly) NSString *id __attribute__((swift_name("id")));
@property (readonly) NSArray<NSString *> *imageUrls __attribute__((swift_name("imageUrls")));
@property (readonly) BOOL isAdAvailable __attribute__((swift_name("isAdAvailable")));
@property (readonly) NSArray<NSString *> *keywords __attribute__((swift_name("keywords")));
@property (readonly) int64_t playlistId __attribute__((swift_name("playlistId")));
@property (readonly) NSString *shareUrl __attribute__((swift_name("shareUrl")));
@property (readonly) NSString *showName __attribute__((swift_name("showName")));
@property (readonly) AMLBurstSource *source __attribute__((swift_name("source")));
@property (readonly) NSString *sourceName __attribute__((swift_name("sourceName")));
@property (readonly) NSString * _Nullable streamUrl __attribute__((swift_name("streamUrl")));
@property (readonly) NSString *title __attribute__((swift_name("title")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("BurstSource")))
@interface AMLBurstSource : AMLBase
- (instancetype)initWithSourceName:(NSString *)sourceName sourceType:(NSString * _Nullable)sourceType showName:(NSString *)showName durationFromStart:(AMLDuration *)durationFromStart audioUrl:(NSString * _Nullable)audioUrl __attribute__((swift_name("init(sourceName:sourceType:showName:durationFromStart:audioUrl:)"))) __attribute__((objc_designated_initializer));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString * _Nullable audioUrl __attribute__((swift_name("audioUrl")));
@property (readonly) AMLDuration *durationFromStart __attribute__((swift_name("durationFromStart")));
@property (readonly) NSString *showName __attribute__((swift_name("showName")));
@property (readonly) NSString *sourceName __attribute__((swift_name("sourceName")));
@property (readonly) NSString * _Nullable sourceType __attribute__((swift_name("sourceType")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("CtaData")))
@interface AMLCtaData : AMLBase
- (instancetype)initWithButtonText:(NSString *)buttonText url:(NSString *)url __attribute__((swift_name("init(buttonText:url:)"))) __attribute__((objc_designated_initializer));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *buttonText __attribute__((swift_name("buttonText")));
@property (readonly) NSString *url __attribute__((swift_name("url")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("DateTime")))
@interface AMLDateTime : AMLBase
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (BOOL)isAfterDateTime:(AMLDateTime *)dateTime __attribute__((swift_name("isAfter(dateTime:)")));
- (BOOL)isBeforeDateTime:(AMLDateTime *)dateTime __attribute__((swift_name("isBefore(dateTime:)")));
- (AMLDateTime *)minusDaysDays:(int64_t)days __attribute__((swift_name("minusDays(days:)")));
- (AMLDateTime *)plusDaysDays:(int64_t)days __attribute__((swift_name("plusDays(days:)")));
- (NSString *)toIsoDateString __attribute__((swift_name("toIsoDateString()")));
- (NSString *)description __attribute__((swift_name("description()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("DateTime.Companion")))
@interface AMLDateTimeCompanion : AMLBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (AMLDateTime * _Nullable)fromIsoDateString:(NSString *)isoDateString __attribute__((swift_name("from(isoDateString:)")));
- (AMLDateTime *)now __attribute__((swift_name("now()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Duration")))
@interface AMLDuration : AMLBase
- (instancetype)initWithValue:(double)value unit:(AMLDurationUnit *)unit __attribute__((swift_name("init(value:unit:)"))) __attribute__((objc_designated_initializer));
@property (readonly) double milliseconds __attribute__((swift_name("milliseconds")));
@property (readonly) double seconds __attribute__((swift_name("seconds")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Duration.Companion")))
@interface AMLDurationCompanion : AMLBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@end;

__attribute__((swift_name("KotlinComparable")))
@protocol AMLKotlinComparable
@required
- (int32_t)compareToOther:(id _Nullable)other __attribute__((swift_name("compareTo(other:)")));
@end;

__attribute__((swift_name("KotlinEnum")))
@interface AMLKotlinEnum<E> : AMLBase <AMLKotlinComparable>
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer));
- (int32_t)compareToOther:(E)other __attribute__((swift_name("compareTo(other:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *name __attribute__((swift_name("name")));
@property (readonly) int32_t ordinal __attribute__((swift_name("ordinal")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("DurationUnit")))
@interface AMLDurationUnit : AMLKotlinEnum<AMLDurationUnit *>
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) AMLDurationUnit *seconds __attribute__((swift_name("seconds")));
@property (class, readonly) AMLDurationUnit *milliseconds __attribute__((swift_name("milliseconds")));
+ (AMLKotlinArray<AMLDurationUnit *> *)values __attribute__((swift_name("values()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Key")))
@interface AMLKey : AMLBase
- (instancetype)initWithKey:(NSString *)key selected:(BOOL)selected __attribute__((swift_name("init(key:selected:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithKey:(NSString *)key segCategory:(NSString *)segCategory source:(NSString *)source sourceId:(int32_t)sourceId position:(int32_t)position selected:(BOOL)selected __attribute__((swift_name("init(key:segCategory:source:sourceId:position:selected:)"))) __attribute__((objc_designated_initializer));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
- (AMLKey *)updateSelectedSelected:(BOOL)selected __attribute__((swift_name("updateSelected(selected:)")));
@property (readonly) NSString *key __attribute__((swift_name("key")));
@property (readonly) int32_t position __attribute__((swift_name("position")));
@property (readonly) NSString *segCategory __attribute__((swift_name("segCategory")));
@property (readonly) BOOL selected __attribute__((swift_name("selected")));
@property (readonly) NSString *source __attribute__((swift_name("source")));
@property (readonly) int32_t sourceId __attribute__((swift_name("sourceId")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("LibraryError")))
@interface AMLLibraryError : AMLKotlinEnum<AMLLibraryError *>
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) AMLLibraryError *network __attribute__((swift_name("network")));
@property (class, readonly) AMLLibraryError *server __attribute__((swift_name("server")));
@property (class, readonly) AMLLibraryError *unexpected __attribute__((swift_name("unexpected")));
@property (class, readonly) AMLLibraryError *wrongapplicationkey __attribute__((swift_name("wrongapplicationkey")));
@property (class, readonly) AMLLibraryError *adurlnotfound __attribute__((swift_name("adurlnotfound")));
@property (class, readonly) AMLLibraryError *nokeysselected __attribute__((swift_name("nokeysselected")));
@property (class, readonly) AMLLibraryError *nosearchresults __attribute__((swift_name("nosearchresults")));
+ (AMLKotlinArray<AMLLibraryError *> *)values __attribute__((swift_name("values()")));
@property (readonly) NSString *message __attribute__((swift_name("message")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("PendingPlaylist")))
@interface AMLPendingPlaylist : AMLBase
- (instancetype)initWithIsReady:(BOOL)isReady playlist:(AMLPlaylist *)playlist __attribute__((swift_name("init(isReady:playlist:)"))) __attribute__((objc_designated_initializer));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) BOOL isReady __attribute__((swift_name("isReady")));
@property (readonly) AMLPlaylist *playlist __attribute__((swift_name("playlist")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("PlaybackState")))
@interface AMLPlaybackState : AMLBase
- (instancetype)initWithUrl:(NSString *)url positionMillis:(int64_t)positionMillis __attribute__((swift_name("init(url:positionMillis:)"))) __attribute__((objc_designated_initializer));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) int64_t positionMillis __attribute__((swift_name("positionMillis")));
@property (readonly) NSString *url __attribute__((swift_name("url")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("PlayerAction")))
@interface AMLPlayerAction : AMLBase
- (instancetype)initWithType:(AMLPlayerActionType *)type value:(NSString *)value __attribute__((swift_name("init(type:value:)"))) __attribute__((objc_designated_initializer));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) AMLPlayerActionType *type __attribute__((swift_name("type")));
@property (readonly) NSString *value __attribute__((swift_name("value")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("PlayerAction.Type_")))
@interface AMLPlayerActionType : AMLKotlinEnum<AMLPlayerActionType *>
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) AMLPlayerActionType *personalized __attribute__((swift_name("personalized")));
@property (class, readonly) AMLPlayerActionType *channel __attribute__((swift_name("channel")));
@property (class, readonly) AMLPlayerActionType *voice __attribute__((swift_name("voice")));
@property (class, readonly) AMLPlayerActionType *search __attribute__((swift_name("search")));
+ (AMLKotlinArray<AMLPlayerActionType *> *)values __attribute__((swift_name("values()")));
@property (readonly) NSString *id __attribute__((swift_name("id")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Playlist")))
@interface AMLPlaylist : AMLBase
- (instancetype)initWithId:(NSString *)id name:(NSString *)name query:(NSString *)query bursts:(NSArray<AMLBurst *> *)bursts playerSessionId:(id)playerSessionId playerAction:(AMLPlayerAction *)playerAction __attribute__((swift_name("init(id:name:query:bursts:playerSessionId:playerAction:)"))) __attribute__((objc_designated_initializer));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSArray<AMLBurst *> *bursts __attribute__((swift_name("bursts")));
@property (readonly) NSString *id __attribute__((swift_name("id")));
@property (readonly) NSString *name __attribute__((swift_name("name")));
@property (readonly) id playerSessionId __attribute__((swift_name("playerSessionId")));
@property (readonly) NSString *query __attribute__((swift_name("query")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("PlaylistInfo")))
@interface AMLPlaylistInfo : AMLBase
- (instancetype)initWithSection:(NSString *)section id:(int32_t)id name:(NSString *)name description:(NSString *)description image:(NSString *)image squareImage:(NSString *)squareImage url:(NSString *)url __attribute__((swift_name("init(section:id:name:description:image:squareImage:url:)"))) __attribute__((objc_designated_initializer));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *description_ __attribute__((swift_name("description_")));
@property (readonly) int32_t id __attribute__((swift_name("id")));
@property (readonly) NSString *image __attribute__((swift_name("image")));
@property (readonly) NSString *name __attribute__((swift_name("name")));
@property (readonly) NSString *section __attribute__((swift_name("section")));
@property (readonly) NSString *squareImage __attribute__((swift_name("squareImage")));
@property (readonly) NSString *url __attribute__((swift_name("url")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Preference")))
@interface AMLPreference : AMLBase
- (instancetype)initWithName:(NSString *)name source:(NSString *)source take:(int32_t)take offer:(int32_t)offer keys:(NSArray<AMLKey *> *)keys iconUrl:(NSString * _Nullable)iconUrl __attribute__((swift_name("init(name:source:take:offer:keys:iconUrl:)"))) __attribute__((objc_designated_initializer));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
- (AMLPreference *)updateKeysKeys:(NSArray<AMLKey *> *)keys __attribute__((swift_name("updateKeys(keys:)")));
@property (readonly) NSString * _Nullable iconUrl __attribute__((swift_name("iconUrl")));
@property (readonly) NSArray<AMLKey *> *keys __attribute__((swift_name("keys")));
@property (readonly) NSString *name __attribute__((swift_name("name")));
@property (readonly) int32_t offer __attribute__((swift_name("offer")));
@property (readonly) NSString *source __attribute__((swift_name("source")));
@property (readonly) int32_t take __attribute__((swift_name("take")));
@end;

__attribute__((swift_name("Result")))
@interface AMLResult<__covariant T> : AMLBase
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ResultData")))
@interface AMLResultData<__covariant T> : AMLResult<T>
- (instancetype)initWithValue:(T _Nullable)value __attribute__((swift_name("init(value:)"))) __attribute__((objc_designated_initializer));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) T _Nullable value __attribute__((swift_name("value")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ResultError")))
@interface AMLResultError : AMLResult<AMLKotlinNothing *>
- (instancetype)initWithError:(AMLLibraryError *)error __attribute__((swift_name("init(error:)"))) __attribute__((objc_designated_initializer));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) AMLLibraryError *error __attribute__((swift_name("error")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("UserPreferences")))
@interface AMLUserPreferences : AMLBase
- (instancetype)initWithId:(NSString *)id userId:(NSString *)userId location:(NSString * _Nullable)location sourceType:(NSString *)sourceType preferences:(NSArray<AMLPreference *> *)preferences __attribute__((swift_name("init(id:userId:location:sourceType:preferences:)"))) __attribute__((objc_designated_initializer));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
- (AMLUserPreferences *)updatePreferencePreferences:(NSArray<AMLPreference *> *)preferences __attribute__((swift_name("updatePreference(preferences:)")));
@property (readonly) NSString *id __attribute__((swift_name("id")));
@property (readonly) NSString * _Nullable location __attribute__((swift_name("location")));
@property (readonly) NSArray<AMLPreference *> *preferences __attribute__((swift_name("preferences")));
@property (readonly) NSString *sourceType __attribute__((swift_name("sourceType")));
@property (readonly) NSString *userId __attribute__((swift_name("userId")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ListenedBurstModel")))
@interface AMLListenedBurstModel : AMLBase
- (instancetype)initWithId:(NSString *)id date_text:(AMLDateTime *)date_text __attribute__((swift_name("init(id:date_text:)"))) __attribute__((objc_designated_initializer));
- (NSString *)component1 __attribute__((swift_name("component1()")));
- (AMLDateTime *)component2 __attribute__((swift_name("component2()")));
- (AMLListenedBurstModel *)doCopyId:(NSString *)id date_text:(AMLDateTime *)date_text __attribute__((swift_name("doCopy(id:date_text:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) AMLDateTime *date_text __attribute__((swift_name("date_text")));
@property (readonly) NSString *id __attribute__((swift_name("id")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ListenedBurstModel.Adapter")))
@interface AMLListenedBurstModelAdapter : AMLBase
- (instancetype)initWithDate_textAdapter:(id<AMLRuntimeColumnAdapter>)date_textAdapter __attribute__((swift_name("init(date_textAdapter:)"))) __attribute__((objc_designated_initializer));
@property (readonly) id<AMLRuntimeColumnAdapter> date_textAdapter __attribute__((swift_name("date_textAdapter")));
@end;

__attribute__((swift_name("ListenedBurstModelQueries")))
@protocol AMLListenedBurstModelQueries <AMLRuntimeTransacter>
@required
- (void)deleteExpiredListenedBurst __attribute__((swift_name("deleteExpiredListenedBurst()")));
- (void)deleteListenedBurstId:(NSString *)id __attribute__((swift_name("deleteListenedBurst(id:)")));
- (void)insertListenedBurstId:(NSString *)id date_text:(AMLDateTime *)date_text __attribute__((swift_name("insertListenedBurst(id:date_text:)")));
- (AMLRuntimeQuery<AMLListenedBurstModel *> *)selectAll __attribute__((swift_name("selectAll()")));
- (AMLRuntimeQuery<id> *)selectAllMapper:(id (^)(NSString *, AMLDateTime *))mapper __attribute__((swift_name("selectAll(mapper:)")));
- (AMLRuntimeQuery<AMLListenedBurstModel *> *)selectAllFromLast30Days __attribute__((swift_name("selectAllFromLast30Days()")));
- (AMLRuntimeQuery<id> *)selectAllFromLast30DaysMapper:(id (^)(NSString *, AMLDateTime *))mapper __attribute__((swift_name("selectAllFromLast30Days(mapper:)")));
@end;

@interface AMLResult (Extensions)
- (AMLResult<id> *)mapF:(id _Nullable (^)(id _Nullable))f __attribute__((swift_name("map(f:)")));
- (AMLResult<id> *)onDataF:(void (^)(id _Nullable))f __attribute__((swift_name("onData(f:)")));
- (AMLResult<id> *)onErrorF:(void (^)(AMLLibraryError *))f __attribute__((swift_name("onError(f:)")));
@property (readonly) AMLLibraryError * _Nullable errorType __attribute__((swift_name("errorType")));
@property (readonly) id _Nullable value_ __attribute__((swift_name("value_")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("DurationKt")))
@interface AMLDurationKt : AMLBase
+ (AMLDuration *)toDuration:(double)receiver unit:(AMLDurationUnit *)unit __attribute__((swift_name("toDuration(_:unit:)")));
@end;

__attribute__((swift_name("RuntimeTransactionCallbacks")))
@protocol AMLRuntimeTransactionCallbacks
@required
- (void)afterCommitFunction:(void (^)(void))function __attribute__((swift_name("afterCommit(function:)")));
- (void)afterRollbackFunction:(void (^)(void))function __attribute__((swift_name("afterRollback(function:)")));
@end;

__attribute__((swift_name("RuntimeTransactionWithoutReturn")))
@protocol AMLRuntimeTransactionWithoutReturn <AMLRuntimeTransactionCallbacks>
@required
- (void)rollback __attribute__((swift_name("rollback()")));
- (void)transactionBody:(void (^)(id<AMLRuntimeTransactionWithoutReturn>))body __attribute__((swift_name("transaction(body:)")));
@end;

__attribute__((swift_name("RuntimeTransactionWithReturn")))
@protocol AMLRuntimeTransactionWithReturn <AMLRuntimeTransactionCallbacks>
@required
- (void)rollbackReturnValue:(id _Nullable)returnValue __attribute__((swift_name("rollback(returnValue:)")));
- (id _Nullable)transactionBody_:(id _Nullable (^)(id<AMLRuntimeTransactionWithReturn>))body __attribute__((swift_name("transaction(body_:)")));
@end;

__attribute__((swift_name("RuntimeCloseable")))
@protocol AMLRuntimeCloseable
@required
- (void)close __attribute__((swift_name("close()")));
@end;

__attribute__((swift_name("RuntimeSqlDriver")))
@protocol AMLRuntimeSqlDriver <AMLRuntimeCloseable>
@required
- (AMLRuntimeTransacterTransaction * _Nullable)currentTransaction __attribute__((swift_name("currentTransaction()")));
- (void)executeIdentifier:(AMLInt * _Nullable)identifier sql:(NSString *)sql parameters:(int32_t)parameters binders:(void (^ _Nullable)(id<AMLRuntimeSqlPreparedStatement>))binders __attribute__((swift_name("execute(identifier:sql:parameters:binders:)")));
- (id<AMLRuntimeSqlCursor>)executeQueryIdentifier:(AMLInt * _Nullable)identifier sql:(NSString *)sql parameters:(int32_t)parameters binders:(void (^ _Nullable)(id<AMLRuntimeSqlPreparedStatement>))binders __attribute__((swift_name("executeQuery(identifier:sql:parameters:binders:)")));
- (AMLRuntimeTransacterTransaction *)doNewTransaction __attribute__((swift_name("doNewTransaction()")));
@end;

__attribute__((swift_name("RuntimeSqlDriverSchema")))
@protocol AMLRuntimeSqlDriverSchema
@required
- (void)createDriver:(id<AMLRuntimeSqlDriver>)driver __attribute__((swift_name("create(driver:)")));
- (void)migrateDriver:(id<AMLRuntimeSqlDriver>)driver oldVersion:(int32_t)oldVersion newVersion:(int32_t)newVersion __attribute__((swift_name("migrate(driver:oldVersion:newVersion:)")));
@property (readonly) int32_t version __attribute__((swift_name("version")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinArray")))
@interface AMLKotlinArray<T> : AMLBase
+ (instancetype)arrayWithSize:(int32_t)size init:(T _Nullable (^)(AMLInt *))init __attribute__((swift_name("init(size:init:)")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (T _Nullable)getIndex:(int32_t)index __attribute__((swift_name("get(index:)")));
- (id<AMLKotlinIterator>)iterator __attribute__((swift_name("iterator()")));
- (void)setIndex:(int32_t)index value:(T _Nullable)value __attribute__((swift_name("set(index:value:)")));
@property (readonly) int32_t size __attribute__((swift_name("size")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinNothing")))
@interface AMLKotlinNothing : AMLBase
@end;

__attribute__((swift_name("RuntimeColumnAdapter")))
@protocol AMLRuntimeColumnAdapter
@required
- (id)decodeDatabaseValue:(id _Nullable)databaseValue __attribute__((swift_name("decode(databaseValue:)")));
- (id _Nullable)encodeValue:(id)value __attribute__((swift_name("encode(value:)")));
@end;

__attribute__((swift_name("RuntimeQuery")))
@interface AMLRuntimeQuery<__covariant RowType> : AMLBase
- (instancetype)initWithQueries:(NSMutableArray<AMLRuntimeQuery<id> *> *)queries mapper:(RowType (^)(id<AMLRuntimeSqlCursor>))mapper __attribute__((swift_name("init(queries:mapper:)"))) __attribute__((objc_designated_initializer));
- (void)addListenerListener:(id<AMLRuntimeQueryListener>)listener __attribute__((swift_name("addListener(listener:)")));
- (id<AMLRuntimeSqlCursor>)execute __attribute__((swift_name("execute()")));
- (NSArray<RowType> *)executeAsList __attribute__((swift_name("executeAsList()")));
- (RowType)executeAsOne __attribute__((swift_name("executeAsOne()")));
- (RowType _Nullable)executeAsOneOrNull __attribute__((swift_name("executeAsOneOrNull()")));
- (void)notifyDataChanged __attribute__((swift_name("notifyDataChanged()")));
- (void)removeListenerListener:(id<AMLRuntimeQueryListener>)listener __attribute__((swift_name("removeListener(listener:)")));
@property (readonly) RowType (^mapper)(id<AMLRuntimeSqlCursor>) __attribute__((swift_name("mapper")));
@end;

__attribute__((swift_name("RuntimeTransacterTransaction")))
@interface AMLRuntimeTransacterTransaction : AMLBase <AMLRuntimeTransactionCallbacks>
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (void)afterCommitFunction:(void (^)(void))function __attribute__((swift_name("afterCommit(function:)")));
- (void)afterRollbackFunction:(void (^)(void))function __attribute__((swift_name("afterRollback(function:)")));
- (void)endTransactionSuccessful:(BOOL)successful __attribute__((swift_name("endTransaction(successful:)")));
@property (readonly) AMLRuntimeTransacterTransaction * _Nullable enclosingTransaction __attribute__((swift_name("enclosingTransaction")));
@end;

__attribute__((swift_name("RuntimeSqlPreparedStatement")))
@protocol AMLRuntimeSqlPreparedStatement
@required
- (void)bindBytesIndex:(int32_t)index bytes:(AMLKotlinByteArray * _Nullable)bytes __attribute__((swift_name("bindBytes(index:bytes:)")));
- (void)bindDoubleIndex:(int32_t)index double:(AMLDouble * _Nullable)double_ __attribute__((swift_name("bindDouble(index:double:)")));
- (void)bindLongIndex:(int32_t)index long:(AMLLong * _Nullable)long_ __attribute__((swift_name("bindLong(index:long:)")));
- (void)bindStringIndex:(int32_t)index string:(NSString * _Nullable)string __attribute__((swift_name("bindString(index:string:)")));
@end;

__attribute__((swift_name("RuntimeSqlCursor")))
@protocol AMLRuntimeSqlCursor <AMLRuntimeCloseable>
@required
- (AMLKotlinByteArray * _Nullable)getBytesIndex:(int32_t)index __attribute__((swift_name("getBytes(index:)")));
- (AMLDouble * _Nullable)getDoubleIndex:(int32_t)index __attribute__((swift_name("getDouble(index:)")));
- (AMLLong * _Nullable)getLongIndex:(int32_t)index __attribute__((swift_name("getLong(index:)")));
- (NSString * _Nullable)getStringIndex:(int32_t)index __attribute__((swift_name("getString(index:)")));
- (BOOL)next __attribute__((swift_name("next()")));
@end;

__attribute__((swift_name("KotlinIterator")))
@protocol AMLKotlinIterator
@required
- (BOOL)hasNext __attribute__((swift_name("hasNext()")));
- (id _Nullable)next_ __attribute__((swift_name("next_()")));
@end;

__attribute__((swift_name("RuntimeQueryListener")))
@protocol AMLRuntimeQueryListener
@required
- (void)queryResultsChanged __attribute__((swift_name("queryResultsChanged()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinByteArray")))
@interface AMLKotlinByteArray : AMLBase
+ (instancetype)arrayWithSize:(int32_t)size __attribute__((swift_name("init(size:)")));
+ (instancetype)arrayWithSize:(int32_t)size init:(AMLByte *(^)(AMLInt *))init __attribute__((swift_name("init(size:init:)")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (int8_t)getIndex:(int32_t)index __attribute__((swift_name("get(index:)")));
- (AMLKotlinByteIterator *)iterator __attribute__((swift_name("iterator()")));
- (void)setIndex:(int32_t)index value:(int8_t)value __attribute__((swift_name("set(index:value:)")));
@property (readonly) int32_t size __attribute__((swift_name("size")));
@end;

__attribute__((swift_name("KotlinByteIterator")))
@interface AMLKotlinByteIterator : AMLBase <AMLKotlinIterator>
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (AMLByte *)next_ __attribute__((swift_name("next_()")));
- (int8_t)nextByte __attribute__((swift_name("nextByte()")));
@end;

#pragma clang diagnostic pop
NS_ASSUME_NONNULL_END
