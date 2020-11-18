#import <Foundation/NSArray.h>
#import <Foundation/NSDictionary.h>
#import <Foundation/NSError.h>
#import <Foundation/NSObject.h>
#import <Foundation/NSSet.h>
#import <Foundation/NSString.h>
#import <Foundation/NSValue.h>

@class AMLBurst, AMLLibraryError, AMLPlaylistInfo, AMLPlaylist, AMLPlaybackState, AMLDuration, AMLBurstSource, AMLDurationUnit, AMLKotlinEnum<E>, AMLResult<__covariant T>, AMLResultData<__covariant T>, AMLResultError, AMLKotlinNothing;

@protocol AMLPlaybackStateListener, AMLKotlinComparable;

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

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("AudioburstLibrary")))
@interface AMLAudioburstLibrary : AMLBase
- (instancetype)initWithApplicationKey:(NSString *)applicationKey __attribute__((swift_name("init(applicationKey:)"))) __attribute__((objc_designated_initializer));
- (void)getAdUrlBurst:(AMLBurst *)burst onData:(void (^)(NSString *))onData onError:(void (^)(AMLLibraryError *))onError __attribute__((swift_name("getAdUrl(burst:onData:onError:)")));
- (void)getPlaylistPlaylistInfo:(AMLPlaylistInfo *)playlistInfo onData:(void (^)(AMLPlaylist *))onData onError:(void (^)(AMLLibraryError *))onError __attribute__((swift_name("getPlaylist(playlistInfo:onData:onError:)")));
- (void)getPlaylistsOnData:(void (^)(NSArray<AMLPlaylistInfo *> *))onData onError:(void (^)(AMLLibraryError *))onError __attribute__((swift_name("getPlaylists(onData:onError:)")));
- (void)removePlaybackStateListenerListener:(id<AMLPlaybackStateListener>)listener __attribute__((swift_name("removePlaybackStateListener(listener:)")));
- (void)setPlaybackStateListenerListener:(id<AMLPlaybackStateListener>)listener __attribute__((swift_name("setPlaybackStateListener(listener:)")));
- (void)start __attribute__((swift_name("start()")));
- (void)stop __attribute__((swift_name("stop()")));
@end;

__attribute__((swift_name("PlaybackStateListener")))
@protocol AMLPlaybackStateListener
@required
- (AMLPlaybackState * _Nullable)getPlaybackState __attribute__((swift_name("getPlaybackState()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Strings")))
@interface AMLStrings : AMLBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)strings __attribute__((swift_name("init()")));
@property (readonly) NSString *errorAdUrlNotFound __attribute__((swift_name("errorAdUrlNotFound")));
@property (readonly) NSString *errorNetwork __attribute__((swift_name("errorNetwork")));
@property (readonly) NSString *errorServer __attribute__((swift_name("errorServer")));
@property (readonly) NSString *errorUnexpected __attribute__((swift_name("errorUnexpected")));
@property (readonly) NSString *errorWrongApplicationKey __attribute__((swift_name("errorWrongApplicationKey")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Burst")))
@interface AMLBurst : AMLBase
- (instancetype)initWithId:(NSString *)id title:(NSString *)title creationDate:(NSString *)creationDate duration:(AMLDuration *)duration sourceName:(NSString *)sourceName category:(NSString * _Nullable)category playlistId:(int64_t)playlistId showName:(NSString *)showName streamUrl:(NSString * _Nullable)streamUrl audioUrl:(NSString *)audioUrl imageUrls:(NSArray<NSString *> *)imageUrls source:(AMLBurstSource *)source shareUrl:(NSString *)shareUrl keywords:(NSArray<NSString *> *)keywords adUrl:(NSString * _Nullable)adUrl __attribute__((swift_name("init(id:title:creationDate:duration:sourceName:category:playlistId:showName:streamUrl:audioUrl:imageUrls:source:shareUrl:keywords:adUrl:)"))) __attribute__((objc_designated_initializer));
- (NSString *)component1 __attribute__((swift_name("component1()")));
- (NSString *)component10 __attribute__((swift_name("component10()")));
- (NSArray<NSString *> *)component11 __attribute__((swift_name("component11()")));
- (AMLBurstSource *)component12 __attribute__((swift_name("component12()")));
- (NSString *)component13 __attribute__((swift_name("component13()")));
- (NSArray<NSString *> *)component14 __attribute__((swift_name("component14()")));
- (NSString *)component2 __attribute__((swift_name("component2()")));
- (NSString *)component3 __attribute__((swift_name("component3()")));
- (AMLDuration *)component4 __attribute__((swift_name("component4()")));
- (NSString *)component5 __attribute__((swift_name("component5()")));
- (NSString * _Nullable)component6 __attribute__((swift_name("component6()")));
- (int64_t)component7 __attribute__((swift_name("component7()")));
- (NSString *)component8 __attribute__((swift_name("component8()")));
- (NSString * _Nullable)component9 __attribute__((swift_name("component9()")));
- (AMLBurst *)doCopyId:(NSString *)id title:(NSString *)title creationDate:(NSString *)creationDate duration:(AMLDuration *)duration sourceName:(NSString *)sourceName category:(NSString * _Nullable)category playlistId:(int64_t)playlistId showName:(NSString *)showName streamUrl:(NSString * _Nullable)streamUrl audioUrl:(NSString *)audioUrl imageUrls:(NSArray<NSString *> *)imageUrls source:(AMLBurstSource *)source shareUrl:(NSString *)shareUrl keywords:(NSArray<NSString *> *)keywords adUrl:(NSString * _Nullable)adUrl __attribute__((swift_name("doCopy(id:title:creationDate:duration:sourceName:category:playlistId:showName:streamUrl:audioUrl:imageUrls:source:shareUrl:keywords:adUrl:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *audioUrl __attribute__((swift_name("audioUrl")));
@property (readonly) NSString * _Nullable category __attribute__((swift_name("category")));
@property (readonly) NSString *creationDate __attribute__((swift_name("creationDate")));
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
- (NSString *)component1 __attribute__((swift_name("component1()")));
- (NSString * _Nullable)component2 __attribute__((swift_name("component2()")));
- (NSString *)component3 __attribute__((swift_name("component3()")));
- (AMLDuration *)component4 __attribute__((swift_name("component4()")));
- (NSString * _Nullable)component5 __attribute__((swift_name("component5()")));
- (AMLBurstSource *)doCopySourceName:(NSString *)sourceName sourceType:(NSString * _Nullable)sourceType showName:(NSString *)showName durationFromStart:(AMLDuration *)durationFromStart audioUrl:(NSString * _Nullable)audioUrl __attribute__((swift_name("doCopy(sourceName:sourceType:showName:durationFromStart:audioUrl:)")));
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
@property (readonly) NSString *message __attribute__((swift_name("message")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("PlaybackState")))
@interface AMLPlaybackState : AMLBase
- (instancetype)initWithUrl:(NSString *)url positionMillis:(int64_t)positionMillis __attribute__((swift_name("init(url:positionMillis:)"))) __attribute__((objc_designated_initializer));
- (NSString *)component1 __attribute__((swift_name("component1()")));
- (int64_t)component2 __attribute__((swift_name("component2()")));
- (AMLPlaybackState *)doCopyUrl:(NSString *)url positionMillis:(int64_t)positionMillis __attribute__((swift_name("doCopy(url:positionMillis:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) int64_t positionMillis __attribute__((swift_name("positionMillis")));
@property (readonly) NSString *url __attribute__((swift_name("url")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Playlist")))
@interface AMLPlaylist : AMLBase
- (instancetype)initWithId:(int32_t)id name:(NSString *)name query:(NSString *)query bursts:(NSArray<AMLBurst *> *)bursts playerSessionId:(id)playerSessionId __attribute__((swift_name("init(id:name:query:bursts:playerSessionId:)"))) __attribute__((objc_designated_initializer));
- (int32_t)component1 __attribute__((swift_name("component1()")));
- (NSString *)component2 __attribute__((swift_name("component2()")));
- (NSString *)component3 __attribute__((swift_name("component3()")));
- (NSArray<AMLBurst *> *)component4 __attribute__((swift_name("component4()")));
- (id)component5 __attribute__((swift_name("component5()")));
- (AMLPlaylist *)doCopyId:(int32_t)id name:(NSString *)name query:(NSString *)query bursts:(NSArray<AMLBurst *> *)bursts playerSessionId:(id)playerSessionId __attribute__((swift_name("doCopy(id:name:query:bursts:playerSessionId:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSArray<AMLBurst *> *bursts __attribute__((swift_name("bursts")));
@property (readonly) int32_t id __attribute__((swift_name("id")));
@property (readonly) NSString *name __attribute__((swift_name("name")));
@property (readonly) id playerSessionId __attribute__((swift_name("playerSessionId")));
@property (readonly) NSString *query __attribute__((swift_name("query")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("PlaylistInfo")))
@interface AMLPlaylistInfo : AMLBase
- (instancetype)initWithId:(int32_t)id name:(NSString *)name description:(NSString *)description image:(NSString * _Nullable)image url:(NSString *)url __attribute__((swift_name("init(id:name:description:image:url:)"))) __attribute__((objc_designated_initializer));
- (int32_t)component1 __attribute__((swift_name("component1()")));
- (NSString *)component2 __attribute__((swift_name("component2()")));
- (NSString *)component3 __attribute__((swift_name("component3()")));
- (NSString * _Nullable)component4 __attribute__((swift_name("component4()")));
- (NSString *)component5 __attribute__((swift_name("component5()")));
- (AMLPlaylistInfo *)doCopyId:(int32_t)id name:(NSString *)name description:(NSString *)description image:(NSString * _Nullable)image url:(NSString *)url __attribute__((swift_name("doCopy(id:name:description:image:url:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly, getter=description_) NSString *description __attribute__((swift_name("description")));
@property (readonly) int32_t id __attribute__((swift_name("id")));
@property (readonly) NSString * _Nullable image __attribute__((swift_name("image")));
@property (readonly) NSString *name __attribute__((swift_name("name")));
@property (readonly) NSString *url __attribute__((swift_name("url")));
@end;

__attribute__((swift_name("Result")))
@interface AMLResult<__covariant T> : AMLBase
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ResultData")))
@interface AMLResultData<__covariant T> : AMLResult<T>
- (instancetype)initWithValue:(T _Nullable)value __attribute__((swift_name("init(value:)"))) __attribute__((objc_designated_initializer));
- (T _Nullable)component1 __attribute__((swift_name("component1()")));
- (AMLResultData<T> *)doCopyValue:(T _Nullable)value __attribute__((swift_name("doCopy(value:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) T _Nullable value __attribute__((swift_name("value")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ResultError")))
@interface AMLResultError : AMLResult<AMLKotlinNothing *>
- (instancetype)initWithError:(AMLLibraryError *)error __attribute__((swift_name("init(error:)"))) __attribute__((objc_designated_initializer));
- (AMLLibraryError *)component1 __attribute__((swift_name("component1()")));
- (AMLResultError *)doCopyError:(AMLLibraryError *)error __attribute__((swift_name("doCopy(error:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) AMLLibraryError *error __attribute__((swift_name("error")));
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

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinNothing")))
@interface AMLKotlinNothing : AMLBase
@end;

#pragma clang diagnostic pop
NS_ASSUME_NONNULL_END
